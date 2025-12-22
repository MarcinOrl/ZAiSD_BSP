# BSP Dungeon – SVG Generator

A Java project that generates **dungeon maps using the BSP (Binary Space Partitioning) algorithm**.  
The output is in **SVG format**, showing three stages: leaf partitions, rooms, and rooms with corridors.

---

## Features

* Generates random dungeon layouts with rooms and corridors.
* Configurable parameters: canvas size, leaf size, room size, padding, seed, and output directory.
* Outputs separate SVG files for each stage:
  1. Partition (leaf rectangles)
  2. Rooms
  3. Rooms + Corridors
* Deterministic generation using a seed.
* Parameters can be provided via **JSON config file** or command line.

---

## Installation and Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/YourUsername/BSPDungeon_SVG.git
   cd BSPDungeon_SVG
