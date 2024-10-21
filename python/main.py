import os
import time
import platform
from itertools import count
from typing import List, Optional
from display import draw_grid, draw, clearScreen, drawEndGame
import random


class GameState():
    playersLocation = (0, 0)
    newPlayersLocation = (0, 0)
    playersPoints = 100
    movesTaken = 0
    playersGold = 0
    playersAvailableShots = 0
    hurdleCount = 0
    goldCount = 0
    hintCount = 0

    gridSize: int
    grid: List[List]

class Position():
    isPlayer = False
    isWumpus = False
    isGold = False
    isPit = False
    isBreeze = False
    isStench = False
    isStartPosition = False
    hasVisited = False

    def __init__(
        self,
        isPlayer: Optional[bool] = False,
        isWumpus: Optional[bool] = False,
        isGold: Optional[bool] = False,
        isPit: Optional[bool] = False,
        isBreeze: Optional[bool] = False,
        isStench: Optional[bool] = False,
        isStartPosition: Optional[bool] = False,
        hasVisited: Optional[bool] = False
    ):
        self.isPlayer = isPlayer
        self.isWumpus = isWumpus
        self.isGold = isGold
        self.isPit = isPit
        self.isBreeze = isBreeze
        self.isStench = isStench
        self.isStartPosition = isStartPosition
        self.hasVisited = hasVisited

    def hasBeenVisited(self):
        self.hasVisited = True

    def placePlayer(self):
        self.isPlayer = True

    def removePlayer(self):
        self.isPlayer = False

    def removeGold(self):
        self.isGold = False

    def addStench(self):
        self.isStench = True

    def addBreeze(self):
        self.isBreeze = True

    def isClear(self, semiClear: Optional[bool]=False):
        # semiClear is used to check if the position is clear excluding the stench and breeze attributes
        if semiClear:
            return not self.isPlayer and not self.isWumpus and not self.isGold and not self.isPit  and not self.isStartPosition
        return not self.isPlayer and not self.isWumpus and not self.isGold and not self.isPit and not self.isBreeze and not self.isStench and not self.isStartPosition

def generateGrid(size):
    grid = [[Position() for _ in range(size)] for _ in range(size)]
    xAxis = random.randint(0, size - 1)
    if xAxis == 0 or xAxis == size - 1:
        yAxis = random.randint(0, size - 1)
    else:
        yAxis = random.choice([0, size - 1])
    grid[xAxis][yAxis] = Position(isPlayer=True, isStartPosition=True, hasVisited=True)
    playersLocation = (xAxis, yAxis)
    return grid, playersLocation

def generateHurdles(grid, gridSize):
    # 25% of the grid will have a hurdle
    maxHurdles = int((gridSize**2)*0.25)
    hurdleCount = 0
    clearZone = []

    for i in range(maxHurdles):
        while True:
            xAxis = random.randint(0, gridSize - 1)
            yAxis = random.randint(0, gridSize - 1)
            if grid[xAxis][yAxis].isClear():
                hurdleCount += 1
                break

        hurdle = random.randint(1, 2)
        if hurdle== 1:
            grid[xAxis][yAxis] = Position(isWumpus=True)
            if xAxis > 0:
                grid[xAxis-1][yAxis].addStench()
            if xAxis < gridSize - 1:
                grid[xAxis+1][yAxis].addStench()
            if yAxis > 0:
                grid[xAxis][yAxis-1].addStench()
            if yAxis < gridSize - 1:
                grid[xAxis][yAxis+1].addStench()
        else:
            grid[xAxis][yAxis] = Position(isPit=True)
            if xAxis > 0:
                grid[xAxis-1][yAxis].addBreeze()
            if xAxis < gridSize - 1:
                grid[xAxis+1][yAxis].addBreeze()
            if yAxis > 0:
                grid[xAxis][yAxis-1].addBreeze()
            if yAxis < gridSize - 1:
                grid[xAxis][yAxis+1].addBreeze()

    return grid, hurdleCount

def generateGold(grid, gridSize):
    maxGold = int((gridSize**2)*0.05)
    if maxGold <= 1:
        # At least 1 gold should be present
        maxGold = random.randint(1, 2)
    goldCount = 0

    for i in range(maxGold):
        while True:
            xAxis = random.randint(0, gridSize - 1)
            yAxis = random.randint(0, gridSize - 1)
            if grid[xAxis][yAxis].isClear(True):
                goldCount += 1
                break

        grid[xAxis][yAxis] = Position(isGold=True)

    return grid, goldCount

def getCurrentOS():
    type = platform.system()
    if type == "Windows":
        return "w"
    elif type == "Linux":
        return "l"
    else:
        return "m"

def getUserInput():
    # Get immediate user input, depending on the OS
    currentOs = getCurrentOS()
    if currentOs == "w":
        import msvcrt
        while True:
                if msvcrt.kbhit():  # Check if a key has been pressed
                    key = msvcrt.getch().decode('utf-8')  # Get the keypress and decode it
                    return key.upper()  # Return uppercase for consistency
    else:
        import sys, tty, termios
        fd = sys.stdin.fileno()
        old_settings = termios.tcgetattr(fd)
        try:
            tty.setraw(sys.stdin.fileno())
            ch = sys.stdin.read(1)
        finally:
            termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
        return ch.upper()

def movePlayer(State, move):
    # Game controls
    # w = up, s = down, a = left, d = right
    if move == "W":
        if State.playersLocation[0] == 0:
            print("You can't move up")
        else:
            State.newPlayersLocation = (State.playersLocation[0] - 1, State.playersLocation[1])
    elif move == "S":
        if State.playersLocation[0] == State.gridSize - 1:
            print("You can't move down")
        else:
            State.newPlayersLocation = (State.playersLocation[0] + 1, State.playersLocation[1])
    elif move == "A":
        if State.playersLocation[1] == 0:
            print("You can't move left")
        else:
            State.newPlayersLocation = (State.playersLocation[0], State.playersLocation[1] - 1)
    elif move == "D":
        if State.playersLocation[1] == State.gridSize - 1:
            print("You can't move right")
        else:
            State.newPlayersLocation = (State.playersLocation[0], State.playersLocation[1] + 1)
    elif move == "Q":
        print("Are you sure you want to quit? (y/n)")
        quitGame = getUserInput()
        if quitGame == "\r" or quitGame == "\n" or quitGame == "Y":
            print("Game Over!")
            os._exit(1)
    elif move == "R":
        pass
    elif move == "E":
        print("Hints:")
        print(f"Number of dangers left: {State.hurdleCount}")
        print(f"Number of gold left: {State.goldCount}")
        State.hintCount += 1
        time.sleep(2)
    else:
        print("Invalid move")

def update(State):
    newPlayerLocation = State.newPlayersLocation
    playerLocation = State.playersLocation
    grid = State.grid

    def move():
        grid[playerLocation[0]][playerLocation[1]].removePlayer()
        grid[newPlayerLocation[0]][newPlayerLocation[1]].placePlayer()
        grid[newPlayerLocation[0]][newPlayerLocation[1]].hasBeenVisited()
        State.playersLocation = newPlayerLocation
        State.playersPoints -= 1
        State.movesTaken += 1

    if grid[newPlayerLocation[0]][newPlayerLocation[1]].isClear(True):
        move()
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isWumpus:
        move()
        print("You were eaten by the Wumpus!")
        State.playersPoints -= 10000
        drawEndGame(State)
        os._exit(1)
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isPit:
        move()
        print("You fell into a pit!")
        State.playersPoints -= 10000
        drawEndGame(State)
        os._exit(1)
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isGold:
        move()
        grid[newPlayerLocation[0]][newPlayerLocation[1]].removeGold()
        State.playersPoints += 1000
        State.goldCount -= 1
        State.playersGold += 1
        draw(State, True)
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isStartPosition and not grid[playerLocation[0]][playerLocation[1]].isStartPosition:
        move()
        print("You're back to the start!")
        print("You just quit mid game, mateee!")
        drawEndGame(State)
        os._exit(1)
    else:
        print("You can't move there, mate!")

def main():
    State = GameState()
    # make sure the grid has at least a 3x3 grid size, for it to work properly
    State.gridSize = 5
    State.grid, playerLocation = generateGrid(State.gridSize)
    State.grid, State.goldCount = generateGold(State.grid, State.gridSize)
    State.playersLocation, State.newPlayersLocation = playerLocation, playerLocation
    State.grid, State.hurdleCount = generateHurdles(State.grid, State.gridSize)

    while True:
        draw(State)
        move = getUserInput()
        movePlayer(State, move)
        update(State)


if __name__ == "__main__":
    main()
