from datetime import datetime
from time import strftime
from flask import Flask, request, jsonify

app = Flask(__name__)

messages = []

# Route that returns connection confirmation
@app.route("/")
def home():
  return "Flask server is running"

# Route to receive a message and return an echo reply with timestamp
@app.route("/message", methods=["POST"])
def message():
  date = datetime.now()
  dateFormatted = date.strftime("%Y-%m-%d %H:%M:%S")

  data = request.get_json()

  text = data.get("text", "")

  response = {
    "recieved": text,
    "reply": f"You said: {text}",
    "date_time": dateFormatted
  }

  messages.append(response)

  return jsonify(response)

# Route to return the status of the order
@app.route("/order-status", methods=["GET"])
def status():

  return jsonify({
    "order_status": "Your order is processing"
  })

# Route to return all messages in the message history
@app.route("/history", methods=["GET"])
def history():
  return jsonify({
    "messages": messages
  })

# Route to return contact email and phone number 
@app.route("/contact-info", methods=["GET"])
def contact():
  return jsonify({
    "email": "dlishcreations@gmail.com",
    "phone": "555-5555"
  })

if __name__ == "__main__":
  app.run(host="0.0.0.0", port=5000, debug=True)