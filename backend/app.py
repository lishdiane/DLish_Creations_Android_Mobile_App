from datetime import datetime
from time import strftime

from flask import Flask, request, jsonify

app = Flask(__name__)

messages = []

@app.route("/")
def home():
  return "Flask server is running"

@app.route("/message", methods=["POST"])
def message():
  data = request.get_json()

  text = data.get("text", "")

  response = {
    "recieved": text,
    "reply": f"You said: {text}"
  }

  messages.append(response)

  return jsonify(response)

@app.route("/time", methods=["GET"])
def time():
  date = datetime.now()
  dateFormatted = date.strftime("%Y-%m-%d %H:%M:%S")

  return jsonify({
    "current_time": dateFormatted
  })

@app.route("/history", methods=["GET"])
def history():
  return jsonify({
    "messages": messages
  })


if __name__ == "__main__":
  app.run(host="0.0.0.0", port=5000, debug=True)