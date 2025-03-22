FROM clojure:temurin-21-tools-deps-bookworm

# Install Python
RUN apt-get update && apt-get install -y --no-install-recommends \
    python3.11 \
    python3-pip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements and install Python packages
COPY requirements.txt .
RUN pip3 install --no-cache-dir -r requirements.txt --break-system-packages

# Install system dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    sudo \
    ca-certificates \
    git \
    curl \
    wget \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

ARG QUARTO_VERSION=1.6.42
RUN ARCH=$(dpkg --print-architecture) \
    && wget https://github.com/quarto-dev/quarto-cli/releases/download/v${QUARTO_VERSION}/quarto-${QUARTO_VERSION}-linux-${ARCH}.deb \
    && dpkg -i quarto-${QUARTO_VERSION}-linux-${ARCH}.deb \
    && rm quarto-${QUARTO_VERSION}-linux-${ARCH}.deb \
    && apt-get update \
    && apt-get install -f -y \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* 

# Install X11 dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    x11-apps \
    xauth \
    libxrender1 \
    libxtst6 \
    libxi6 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# Switch to non-root user
USER $USERNAME

# Set environment variables
ENV DISPLAY=:0

# Add NVIDIA GPU support
ENV NVIDIA_VISIBLE_DEVICES=all
ENV NVIDIA_DRIVER_CAPABILITIES=compute,utility

# Set working directory
WORKDIR /workspaces