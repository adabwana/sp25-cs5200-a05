#!/bin/bash

# Enable X11 forwarding for local connections
xhost +local:*

# Check if DISPLAY is set
if [ -z "$DISPLAY" ]; then
    echo "DISPLAY environment variable is not set!"
    echo "Please make sure you have an X server running."
    exit 1
fi

echo "X11 forwarding is set up. DISPLAY=$DISPLAY"
echo "You can now run the Tic Tac Toe game with: clojure -M -m tictactoe.main" 