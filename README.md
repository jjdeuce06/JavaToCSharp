# JavaToCSharp

A collection of old Java coursework assignments, rewritten in C#. This repo is a personal exercise in porting working Java programs over to C# / .NET — same logic and requirements, idiomatic C# implementation. Mainly done to sharpen my C# before starting a new job.

## Requirements

- [.NET 10 SDK](https://dotnet.microsoft.com/download)

## Assignments

Each assignment lives in its own folder:

```
JavaToCSharp/
├── Assignment1/
│   ├── Assignment1.csproj
│   └── *.cs
├── Assignment2/
│   ├── Assignment2.csproj
│   └── *.cs
└── ...
```

| Assignment | Description |
|---|---|
| `Assignment1` | Reads grades from user input and outputs their sum, count, and average. |
| `Assignment2` | Prompts for and validates an input file and an output file (from user input or command line), reading the input line by line, tokenizing it, and classifying each token as a word or a number. Numbers are summed; words are tracked in order along with their occurrence counts. Writes the word list, per-word counts, total unique word count, and the total sum of numbers to the output file. |
| `Assignment3` | A GUI directory browser. Lists the contents of a directory (starting from a command-line argument or the current directory), lets the user drill into subdirectories or go up to the parent, and copies a selected source file to a chosen target path, creating or overwriting it as needed. Validates input and shows errors for missing or invalid paths. |
| `Assignment4` | A Swing animation using a null layout manager. Animates a bouncing circle/square on its own thread, with controls to adjust speed and size, run/pause, toggle a motion tail, clear the canvas, and quit. Handles window and component resize events, and keeps the animated object's size in sync with its scrollbar and within the bounds of the canvas. |
| `Assignment5` | An extension of the bounce animation using BorderLayout (for the bouncing ball) and GridBagLayout (for the controls). Adds mouse-driven rectangles: click-and-drag draws a box that becomes a solid obstacle on release, and clicking an existing rectangle removes it. The ball bounces off and resizes relative to the canvas edges and any rectangles present. Runs on its own thread. |
| `Assignment6` | A further extension of the bounce program adding a menu-driven UI (run/pause/stop/quit, gravity and size/speed toggles), a polygon-based cannon, and projectile physics. Gravity affects the ball's motion; obstacle rectangles work as in Assignment5. Firing the cannon can destroy rectangles, the ball, or the opposing cannon, scoring a point for the computer or the player depending on what's hit. Runs on its own thread. |
| `Assignment7` | A NetworkChat client/server application built with GridBagLayout. Uses a `ServerSocket` and client sockets to connect two instances for two-way communication, with the program able to act as either client or server. A text area shows connection status and labels each message as incoming or outgoing. Runs on its own thread. |

## Notes on the conversion

A few recurring Java → C# swaps that come up throughout this repo:

| Java | C# |
|---|---|
| `BufferedReader` / `InputStreamReader(System.in)` | `StreamReader` / `Console.In` |
| `System.out.println` | `Console.WriteLine` |
| `String.isEmpty()` | `string.IsNullOrEmpty(s)` |
| `throws IOException` | *(no equivalent — C# has no checked exceptions)* |
| `boolean` | `bool` |

C# also reserves `in` and `out` as keywords, so variable names that were fine in Java (like `in`/`out` for readers and writers) need to be renamed.

## Status

Work in progress — assignments are being ported over one at a time.
