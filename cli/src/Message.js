export class Message {
  static fromJSON(buffer) {
    return new Message(JSON.parse(buffer.toString()))
  }

  constructor({
    username,
    command,
    contents
  }) {
    this.username = username
    this.command = command
    this.contents = contents
    this.lastCommand = "echo"
    let a = new Date
    this.date = `${a.getMonth()}/${a.getDate()}/${a.getYear() - 100}-${a.getHours()}:${a.getMinutes()}:${a.getSeconds()}:${a.getMilliseconds()}`
  }

  toJSON() {
    return JSON.stringify({
      username: this.username,
      command: this.command,
      contents: this.contents,
      date: this.date
    })
  }

  toString() {
    let mes;
    if (this.command === "at") {
      this.username = "<" + this.username + ">"
      mes = "(whisper):";
    } else
    if (this.command === "broadcast") {
      this.username = "<" + this.username + ">"
      mes = "(all):";
    } else
    if (this.command === "echo") {
      this.username = "<" + this.username + ">"
      mes = "(echo):";
    } else {
      this.username = "";
      mes = ""
    }
    return "{" + this.date + "}: " + this.username + " " + mes + " " + this.contents;
  }
}
