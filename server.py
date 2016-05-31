from flask import Flask
  
app = Flask(__name__)
  
@app.route('/open')
def open():
  print 'Abrindo'

app.run()