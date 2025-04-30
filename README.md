# 2D Platformer Game Engine

This project is a **simple, extensible 2D core engine** for platformer-style games, built with [LibGDX](https://libgdx.com/). It provides essential components such as an entity system, physics engine (gravity & jumping), tile management (including animated tiles), and a basic rendering pipeline with camera tracking and debug tools.

This engine is designed as a base upon which full games can be developed, making it ideal for educational purposes, prototyping, or lightweight core projects.

https://github.com/user-attachments/assets/acac2667-bca4-44d1-8398-9e566dd4cbe7

## Features

- **Entity System**: Abstract base class for players, enemies, or any moving object.
- **Physics Engine**:
    - Gravity simulation
    - Jump mechanics
    - Configurable constants for tuning
- **Tile Engine**:
    - Supports both static and animated tiles
    - Tile collision via `Rectangle` hitboxes
- **Rendering System**:
    - Sprite animation using `TextureRegion`
    - Camera follows the player while clamped to the map boundaries
    - Debug rendering with `ShapeRenderer`
- **Input Handling**: Modular input system for controlling the player
- **Animation Configs**: Per-tile animation frame settings using a config map

## Architecture Overview
- src/
- ├── **core/** 
- └─── Game.java # Core LibGDX core lifecycle (create, render, dispose)
- ├── **entities/**
- ├─── Entity.java # Base entity class (position, hitboxes, movement)
- ├─── Player.java # Handles player logic
- └─── Enemy.java # Simple enemy with basic tracking AI
- ├─── **inputs/**
- └─── InputsManager.java # Keyboard input mapping to player actions
- ├── **physics/**
- ├─── GravityPhysics.java # Applies gravity over time
- └─── JumpPhysics.java # Handles upward motion and jump physics
- ├── **tileManager/**
- ├─── TileManager.java # Loads and renders tiles from folder
- ├─── Tile.java # Base tile class
- ├─── AnimatedTile.java # Tiles with frame-based animation
- └─── TileInstance.java # Individual tile with collision data
- ├── **utilities/**
- ├─── AnimatedConfig.java # Frame config per animated tile ID
- └─── **constants/**
- ├───── AudiConstants.java
- ├───── EntityConstants.java
- ├───── MapTilesConstants.java
- ├───── PhysicsConstants.java
- ├───── TextureConstants.java
- └───── FramesConstants.java
- ├── **menu/**
- ├── GameOverScreen.java
- ├─── GameScreen.java
- ├─── MainMenuScreen.java
- ├─── OptionsScreen.java
- └─── PauseScreen.java
- ├── **audio/**
- ├─── MusicController.java
- └─── SoundController.java



# ️Setting Project on Docker

## Method 1 (Recommended) : pull from dockerhub and run it.
```bash
xhost +local:docker  # Allow Docker access to the display
```
```bash
docker run -it --rm
--net=host
-e DISPLAY=$DISPLAY
-v /tmp/.X11-unix:/tmp/.X11-unix
--device /dev/snd   bighes121/2dgameengine:v5
````

## Method 2: load the image and run it.

```bash
docker load -i 2d-game-engine.tar
```
```bash
docker run -it --rm \
--net=host \
-e DISPLAY=$DISPLAY \
-v /tmp/.X11-unix:/tmp/.X11-unix \
--device /dev/snd \
2dgameengine:latest
```
## Requirements

- Java 21+
- [LibGDX Setup](https://libgdx.com/wiki/start/project-generation) with Gradle
- IDE like IntelliJ IDEA or VS Code

## Setup (without Docker)

1. Clone the repository:
   ```bash
   git clone https:https: //github.com/BigB021/2D-Game-Engine.git
   ```
2. Open the project in your IDE (Intellij) and load Gradle.
3. Run the desktop launcher via Game.java.

 Debug Tools
The engine includes debug rendering:
• Blue rectangle → Player hitbox
• Red rectangle → Enemy hitbox
• Yellow rectangle → Player attack range
• Green rectangle → Enemy attack range


## License
[MIT License](LICENSE)

## Contributors
- [Youssef Aitbouddroub](https://github.com/BigB021)
- [Mohamed Amine Amda](https://github.com/bighes121)
- [Houssam Elaoutmani](https://github.com/houssamelaoutmani)
- [Soufiane El Amraoui](https://github.com/SEL1000)

