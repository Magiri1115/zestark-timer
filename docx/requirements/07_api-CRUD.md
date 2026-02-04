# API単位でのCURD
```
| API                     | 許可状態       　|Session更新　|Event記録|
| ----------------------- | --------------- | ---------- | ------- |
| POST /sessions/start    | idle            | C(running) | C       |
| POST /sessions/pause    | running         | U(paused)  | C       |
| POST /sessions/resume   | paused          | U(running) | C       |
| POST /sessions/end      | running, paused | U(ended)   | C       |
| POST /sessions/auto-end | running         | U(ended)   | C       |
```