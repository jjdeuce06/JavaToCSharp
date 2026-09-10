# JavaToCSharp

A collection of old Java coursework assignments, rewritten in C#. This repo is a personal exercise in porting working Java programs over to C# / .NET — same logic and requirements, idiomatic C# implementation.

## Structure

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

Update the folder names above to match your actual assignments as you add them.

## Requirements

- [.NET 10 SDK](https://dotnet.microsoft.com/download)

## Building and running

From inside a specific assignment's folder:

```bash
dotnet build
dotnet run
```

Or, from the repo root, target a specific project:

```bash
dotnet run --project Assignment1
```

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
