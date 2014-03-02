--maze generator
--small bug fix, formatting change

--useage: mazeGen <width> <height>

--Andrew Guttman
--3/1/2014

import System.Random

--setup
makeMaze :: Int -> Int -> [Int] -> ([[Bool]],[[Bool]])
makeMaze width height (a:b:cs) = do
    let rWalls = (make2D width height True)
    let bWalls = (make2D width height True)
    let visited = (make2D width height False)
    let i = (mod (abs a) width , mod (abs b) height) --needs random value (Int, Int) between (0 and width -1, 0 and height -1)
    let n = [(-1,-1)] :: [(Int, Int)]
    let (v, r, b, c) = visit (visited, rWalls, bWalls, cs) i width height n
    (r, b)

--dfs
visit :: ([[Bool]],[[Bool]],[[Bool]],[Int]) -> (Int, Int) -> Int -> Int -> [(Int,Int)] -> ([[Bool]],[[Bool]],[[Bool]],[Int])
visit (visited, rWalls, bWalls, cs) _ _ _ [] = (visited, rWalls, bWalls, cs)
visit (visited, rWalls, bWalls, []) _ _ _ _ = ([[True]], [[True]], [[True]], [])
visit (visited, rWalls, bWalls, (c:cs)) here width height n = do
    let v = replace2D here True visited
    let ns = neighborCheck n here width height
    let j = mod (abs c) (length ns) --needs random Int between 0 and (length ns -1)
    let there = ns !! j
    let seen = read2D there visited
    (if seen == True then visit (v, rWalls, bWalls, cs) here width height (take j ns ++ drop (j + 1) ns) 
        else visit (visit (v, (removeRWall here there rWalls), (removeBWall here there bWalls), cs) there width height [(-1,-1)])
        here width height (take j ns ++ drop (j + 1) ns))

--bullshit
neighborCheck :: [(Int, Int)] -> (Int, Int) -> Int -> Int -> [(Int, Int)]
neighborCheck [(-1, -1)] here width height = neighbors here width height
neighborCheck list _ _ _= list

--dfs helper
neighbors :: (Int, Int) -> Int -> Int -> [(Int, Int)]
neighbors (x, y) width height = (if x == 0            then [] else [(x-1, y  )])++
                               (if x == (width - 1)  then [] else [(x+1, y  )])++
                               (if y == 0            then [] else [(x  , y-1)])++
                               (if y == (height - 1) then [] else [(x  , y+1)])

--connects horizontal
removeRWall :: (Int, Int) -> (Int, Int) -> [[Bool]] -> [[Bool]]
removeRWall (x1, y1) (x2, y2) walls
    | y1 == y2 = replace2D (min x1 x2, min y1 y2) False walls
    | otherwise = walls

--connects vertical
removeBWall :: (Int, Int) -> (Int, Int) -> [[Bool]] -> [[Bool]]
removeBWall (x1, y1) (x2, y2) walls
    | x1 == x2 = replace2D (min x1 x2, min y1 y2) False walls
    | otherwise = walls

--prints
maze :: Int -> Int -> [Int] -> IO()
maze x y list =  putStr (strMaze (makeMaze x y list) (0,0) y)

--constructs string
strMaze :: ([[Bool]],[[Bool]]) -> (Int, Int) -> Int -> String
strMaze (rWalls, bWalls) (0, y) height = strTop rWalls ++ strTop rWalls ++ strMaze (rWalls, bWalls) (1, y) height
strMaze (rWalls, bWalls) (x, y) height = 
    (if y == height then "\n" else "00   " ++ strR rWalls (x, y) ++ "00   " ++ strR rWalls (x, y) ++ strB bWalls (x, y) ++ strB bWalls (x, y) ++ strMaze (rWalls, bWalls) (x, y+1) height)

strTop :: [[Bool]] -> String
strTop [] = "00\n"
strTop (x:xs) = "00000" ++ strTop xs

strR :: [[Bool]] -> (Int, Int) -> String
strR rWalls (x, y) 
    | x == (length rWalls + 1) = "\n"
    | otherwise = (if read2D (x-1, y) rWalls == True then "00   " else "     ") ++ strR rWalls (x + 1, y)

strB :: [[Bool]] -> (Int, Int) -> String
strB bWalls (x, y)
    | x == (length bWalls + 1) = "00\n"
    | otherwise = "00" ++ (if read2D (x-1, y) bWalls == True then "000" else "   ") ++ strB bWalls (x + 1, y)

--fakes 2D array
make1D :: Int -> Bool -> [Bool]
make1D 0 _ = []
make1D num val = [val] ++ make1D (num-1) val

make2D :: Int -> Int -> Bool -> [[Bool]]
make2D 0 _ _ = [] 
make2D num1 num2 val = [make1D num2 val] ++ make2D (num1-1) num2 val

replace1D :: Int -> Bool -> [Bool] -> [Bool]
replace1D 0 val (x:xs) = val:xs
replace1D num val(x:xs) = x:replace1D (num-1) val xs

replace2D :: (Int,Int) -> Bool -> [[Bool]] -> [[Bool]]
replace2D (0,num2) val (x:xs) = (replace1D num2 val x):xs
replace2D (num1,num2) val (x:xs) = x:replace2D ((num1-1),num2) val xs

read2D :: (Int, Int) -> [[Bool]] -> Bool
read2D (num1,num2) list = (list !! num1) !! num2

mazeGen x y = do
    g <- newStdGen
    let list = take (x * y * 4) (randoms g :: [Int])
    maze x y list