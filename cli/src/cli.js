import vorpal from 'vorpal'
import {
  words
} from 'lodash'
import {
  connect
} from 'net'
import {
  Message
} from './Message'

export const cli = vorpal()

let username
let server
let lastCommand

cli
  .delimiter(cli.chalk['yellow']('ftd~$'))

cli
  .mode('connect <ipAddress> <port> <username>')
  .delimiter(cli.chalk['green']('<connected>'))
  .init(function(args, callback) {
    username = args.username
    server = connect({
      host: args.ipAddress,
      port: args.port
    }, () => {
      server.write(new Message({
        username,
        command: 'connect'
      }).toJSON() + '\n')
      callback()
    })

    server.on('data', (buffer) => {
      let msg = Message.fromJSON(buffer)
      let mes;
      if (msg.command === "at") {
        msg.username = "<" + msg.username + ">"
        mes = cli.chalk.red("(whisper):");
      } else
      if (msg.command === "broadcast") {
        msg.username = "<" + msg.username + ">"
        mes = cli.chalk.yellow("(all):");
      } else
      if (msg.command === "echo") {
        msg.username = "<" + msg.username + ">"
        mes = cli.chalk.green("(echo):");
      } else {
        msg.username = "";
        mes = ""
      }
      this.log(cli.chalk.magenta("{" + msg.date + "}: ") + cli.chalk.blue(msg.username) + " " + mes + " " + msg.contents);

    })

    server.on('end', () => {
      cli.exec('exit')
    })
  })
  .action(function(input, callback) {
    let [command, ...rest] = words(input)
    const contents = rest.join(' ')

    if (command === 'disconnect') {
      server.end(new Message({
        username,
        command
      }).toJSON() + '\n')
    } else if (command === 'echo') {
      server.write(new Message({
        username,
        command,
        contents
      }).toJSON() + '\n')
    } else if (command === "broadcast") {
      server.write(new Message({
        username,
        command,
        contents
      }).toJSON() + '\n')
    } else if (command === 'users') {
      server.write(new Message({
        username,
        command,
        contents
      }).toJSON() + '\n')
      this.log(cli.chalk['blue']("Getting users"))
    } else if (input.startsWith("@")) {
      server.write(new Message({
        username,
        command: "at",
        contents: command + " " + contents
      }).toJSON() + "\n")
    } else {
      this.log(`Command <${command}> was not recognized, last command <${lastCommand}>`)
    }
    callback()
  })
