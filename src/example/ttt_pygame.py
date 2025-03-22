import pygame
import sys
import random

class TicTacToe:
    
    def __init__(self, size=9, cell_size=50):
        """
        Initialize the Tic-Tac-Toe game.
        
        Parameters:
           - size (int): The size of the game board (default is 9x9).
           - cell_size (int): The pixel size of each cell in the grid.
        """
        self.size = size
        self.cell_size = cell_size
        # Create an empty board
        self.board = [[' ' for _ in range(size)] for _ in range(size)]
        self.current_player = 'X'  # X always starts the game
        pygame.init()
        # Set up the game window
        self.screen = pygame.display.set_mode((size * cell_size, size * cell_size))
        pygame.display.set_caption("Tic-Tac-Toe")
        # Set font for text rendering
        self.font = pygame.font.Font(None, 50)
        self.running = True  # Game loop control flag
        self.winner_message = None  # Message displayed when there is a winner
    
    def draw_board(self):
        """
        Draw the game board and update the display.
        """
        self.screen.fill((255, 255, 255))  # Fill the background with white
        for row in range(self.size):
            for col in range(self.size):
                # Define cell boundaries
                rect = pygame.Rect(col * self.cell_size, row * self.cell_size, self.cell_size, self.cell_size)
                pygame.draw.rect(self.screen, (0, 0, 0), rect, 1)  # Draw grid lines
                
                # Render X or O if present
                if self.board[row][col] != ' ':
                    text_surface = self.font.render(self.board[row][col], True, (0, 0, 0))
                    self.screen.blit(text_surface, (col * self.cell_size + 15, row * self.cell_size + 10))
        
        # Display the winner message if there is one
        if self.winner_message:
            text_surface = self.font.render(self.winner_message, True, (255, 0, 0))
            self.screen.blit(text_surface, (10, 10))
        
        pygame.display.flip()  # Update the screen
    
    def reset_game(self):
        """
        Reset the game board and state to start a new round.
        """
        self.board = [[' ' for _ in range(self.size)] for _ in range(self.size)]
        self.current_player = 'X'
        self.winner_message = None
        self.running = True
    
    def place_mark(self, row, col, mark):
        """
        Place a player's mark ('X' or 'O') on the board if the cell is empty.

        Parameters:
           - row (int): Row index of the board.
           - col (int): Column index of the board.
           - mark (str): Player's mark ('X' or 'O').

        Returns:
           - bool: True if the move is valid, False otherwise.
        """
        if 0 <= row < self.size and 0 <= col < self.size and self.board[row][col] == ' ':
            self.board[row][col] = mark
            return True
        return False  # Cell is already occupied or out of bounds
    
    def is_full(self):
        """
        Check if the board is completely filled with no empty spaces.

        Returns:
           - bool: True if the board is full, False otherwise.
        """
        return all(cell != ' ' for row in self.board for cell in row)
    
    def check_winner(self, mark):
        """
        Check if a player has won the game by forming a line of three marks.

        Parameters:
           - mark (str): The player's mark ('X' or 'O').

        Returns:
           - bool: True if the player has won, False otherwise.
        """
        # Check horizontal win conditions
        for row in range(self.size):
            for col in range(self.size - 2):
                if all(self.board[row][col + i] == mark for i in range(3)):
                    return True
        
        # Check vertical win conditions
        for col in range(self.size):
            for row in range(self.size - 2):
                if all(self.board[row + i][col] == mark for i in range(3)):
                    return True
        
        # Check diagonal win conditions
        for row in range(self.size - 2):
            for col in range(self.size - 2):
                # Check main diagonal (top-left to bottom-right)
                if all(self.board[row + i][col + i] == mark for i in range(3)):
                    return True
                # Check anti-diagonal (top-right to bottom-left)
                if all(self.board[row + i][col + 2 - i] == mark for i in range(3)):
                    return True
                
        return False  # No winner found
    
    def best_move(self):
        """
        Select a random available move for the computer (O) and place it on the board.
        """
        # Get all empty cells
        available_moves = [(row, col) for row in range(self.size) for col in range(self.size) if self.board[row][col] == ' ']
        
        if available_moves:
            row, col = random.choice(available_moves)  # Choose a random move
            self.place_mark(row, col, 'O')  # Place 'O' in the chosen cell
    
    def handle_click(self, pos):
        """
        Handle a mouse click event to place 'X' and trigger the computer's move.

        Parameters:
           - pos (tuple): The (x, y) position of the mouse click.
        """
        if self.winner_message:  # Ignore clicks if the game has ended
            return
        
        # Convert pixel position to board indices
        col = pos[0] // self.cell_size
        row = pos[1] // self.cell_size

        # Place 'X' and check for a win or tie
        if self.place_mark(row, col, 'X'):
            if self.check_winner('X'):
                self.winner_message = "X Wins! R: Replay, Q: Quit"
                return
            elif self.is_full():
                self.winner_message = "Tie! R: Replay, Q: Quit"
                return
            
            # AI move (O) after player move
            self.best_move()

            # Check if AI (O) wins or the game ties
            if self.check_winner('O'):
                self.winner_message = "O Wins! R: Replay, Q: Quit"
                return
            elif self.is_full():
                self.winner_message = "Tie! R: Replay, Q: Quit"
                return
    
    def run(self):
        """
        Main game loop that handles events and updates the game state.
        """
        while self.running:
            for event in pygame.event.get():
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_q:  # Quit the game
                        self.running = False
                    elif event.key == pygame.K_r:  # Reset the game
                        self.reset_game()
                elif event.type == pygame.MOUSEBUTTONDOWN:
                    self.handle_click(pygame.mouse.get_pos())  # Handle a player move
                    
            self.draw_board()  # Refresh the game screen

        pygame.quit()
        sys.exit()  # Exit the game
    
# Game settings
BOARD_SIZE = 9  # 9x9 board
CELL_SIZE = 50  # Each cell is 50 pixels

# Start the game
game = TicTacToe(size=BOARD_SIZE, cell_size=CELL_SIZE)
game.run()
