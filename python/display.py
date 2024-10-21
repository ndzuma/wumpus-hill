import os
import time
from rich import print
from typing import List
from subprocess import call

def banner():
    font = """[bold blue]

██╗    ██╗██╗   ██╗███╗   ███╗██████╗ ██╗   ██╗███████╗███████╗███████╗
██║    ██║██║   ██║████╗ ████║██╔══██╗██║   ██║██╔════╝██╔════╝██╔════╝
██║ █╗ ██║██║   ██║██╔████╔██║██████╔╝██║   ██║███████╗███████╗███████╗
██║███╗██║██║   ██║██║╚██╔╝██║██╔═══╝ ██║   ██║╚════██║╚════██║╚════██║
╚███╔███╔╝╚██████╔╝██║ ╚═╝ ██║██║     ╚██████╔╝███████║███████║███████║
 ╚══╝╚══╝  ╚═════╝ ╚═╝     ╚═╝╚═╝      ╚═════╝ ╚══════╝╚══════╝╚══════╝


"""
    print(font)

def draw(State, notification: bool = False):
    def goldFound():
        print("\n[yellow]You found gold![/]\n")
        if State.playersGold == 1:
            print("You have found ", State.playersGold, " gold bar now!")
        else:
            print("You have found ", State.playersGold, " gold bars now!")
        print("You have ", State.playersPoints, " points now!")
        time.sleep(2)

    clearScreen()
    banner()
    print("Stats:\n")
    print("Player's Points: ", State.playersPoints)
    print("Player's Gold: ", State.playersGold)
    print("Player's Available Shots: ", State.playersAvailableShots, "\n")
    print("Legend:\n")
    print("🧍🏽‍♂️ = Player, 🏠 = Start Position")
    print("[yellow]Gld[/] = Gold, [red]b[/] = Breeze, [red]s[/] = Stench\n")
    draw_grid(State.gridSize, State.grid)
    print("\nControls:")
    print("\nw = up, s = down, a = left, d = right")
    print("q = quit, e = Hint\n")
    print("Note: Just avoid the arrow keys\n")
    if notification:
        goldFound()



def drawEndGame(State):
    clearScreen()
    banner()
    print("Stats:\n")
    print("Points: ", State.playersPoints)
    print("Gold: ", State.playersGold)
    print("Moves taken: ", State.movesTaken)
    print("Hints used: ", State.hintCount)

    print("\nLast frame:\n")
    draw_grid(State.gridSize, State.grid)
    print("\nGame Over!\n")


def draw_grid(size: int, grid: List[List[int]]):
    line1 = ""
    for i in range(size):
        line1+= "+⎯⎯⎯⎯⎯⎯"
        if i == size - 1:
            line1+= "+"

    for x in range(size):
        line2 = ""
        for y in range(size):
            if grid[x][y].hasVisited:
                if grid[x][y].isClear():
                    line2+= "|      "
                elif grid[x][y].isPlayer:
                    if grid[x][y].isBreeze and grid[x][y].isStench:
                        line2+= "| 🧍🏽‍♂️ [red]bs[/]"
                    elif grid[x][y].isBreeze:
                        line2+= "|  🧍🏽‍♂️ [red]b[/]"
                    elif grid[x][y].isStench:
                        line2+= "|  🧍🏽‍♂️ [red]s[/]"
                    else:
                        line2+= "|  🧍🏽‍♂️  "
                elif grid[x][y].isStartPosition:
                    line2+= "|  🏠  "
                elif grid[x][y].isWumpus:
                    line2+= "|   W  "
                elif grid[x][y].isGold:
                    if grid[x][y].isBreeze and grid[x][y].isStench:
                        line2+= "|[yellow]Gld[/] [red]bs[/]"
                    elif grid[x][y].isBreeze:
                        line2+= "| [yellow]Gld[/] [red]b[/]"
                    elif grid[x][y].isStench:
                        line2+= "| [yellow]Gld[/] [red]s[/]"
                    else:
                        line2+= "|  [yellow]Gld[/] "
                elif grid[x][y].isPit:
                    line2+= "|  Pi  "
                elif grid[x][y].isBreeze and grid[x][y].isStench:
                    line2+= "|  [red]bs[/]  "
                elif grid[x][y].isBreeze:
                    line2+= "|   [red]b[/]  "
                elif grid[x][y].isStench:
                    line2+= "|   [red]s[/]  "
                else:
                    line2+= "|     "
            else:
                line2+= "|   ?  "
            if y == size - 1:
                line2+= "|"
        print(line1)
        print(line2)
    print(line1)


def clearScreen():
    # check and make call for specific operating system
    _ = call('clear' if os.name == 'posix' else 'cls')
