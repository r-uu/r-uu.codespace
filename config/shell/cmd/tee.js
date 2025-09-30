// tee.js - JScript (WSH)
// Liest von stdin und schreibt jede Zeile sowohl in die angegebene Logdatei als auch auf stdout.
// Aufruf aus cmd:  somecommand 2>&1 | cscript //nologo path\to\tee.js path\to\logfile

(function(){
  var fso = new ActiveXObject("Scripting.FileSystemObject");
  var args = WScript.Arguments;
  if (args.length < 1) {
    WScript.StdErr.WriteLine("Usage: tee.js logfile");
    WScript.Quit(1);
  }
  var logPath = args.Item(0);
  // Modus: 8 = ForAppending, true = create if not exists, 0 = ASCII/ANSI
  var log = fso.OpenTextFile(logPath, 8 /*ForAppending*/, true /*create*/, 0 /*TristateFalse*/);
  var stdin = WScript.StdIn;
  var stdout = WScript.StdOut;
  while (!stdin.AtEndOfStream) {
    var line = stdin.ReadLine();
    log.WriteLine(line);
    stdout.WriteLine(line);
  }
  log.Close();
  WScript.Quit(0);
})();

