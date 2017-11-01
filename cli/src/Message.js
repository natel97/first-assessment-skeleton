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
    this.date = `${a.getMonth()}/${a.getDate()}/${a.getYear() - 100}  -  ${a.getHours()}:${a.getMinutes()}:${a.getSeconds()}:${a.getMilliseconds()}`
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
    return this.contents
  }
}
