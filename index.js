var mysql = require('mysql');
var bodyParser = require('body-parser')

const express        = require('express');
const port = 8000;
const app            = express();
app.use( bodyParser.json() );       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
})); 
var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
  database:"expenses"
});

con.connect(function(err) {
 console.log("Connected");

  
});

app.post('/addRecord', (req, res) => {   
    
      let data = {title: req.body.title, amount: req.body.amount,type: req.body.type};

    let sql = "INSERT INTO `records` SET ?";
    let query = con.query(sql, data,(err, results) => {
      if(err){
        res.setHeader('Content-Type', 'application/json');
        res.end(JSON.stringify({ "message": err,"res":"failed" }));
      }else{
        res.setHeader('Content-Type', 'application/json');
        res.end(JSON.stringify({ "message": "user added","res":"success" }));
      }
       
      
    });
        
  });


  app.post('/getRecords', (req, res) => {   
    let sql = "SELECT * FROM records";
  let query = con.query(sql, (err, results) => {
      console.log(results.length);
      res.setHeader('Content-Type', 'application/json');
      if(results.length !=0){

        var json = JSON.stringify({ 
            "res": "success", 
            "data": results, 
            "message": "data Received"
          });
          res.end(json);
      }else{
        var json = JSON.stringify({ 
            "res": "failed", 
            "data": results, 
            "message": "No data Received"
          });
          res.end(json);
      }
    
  });
        
  });

  app.post('/removeRecord', (req, res) => {   
    
      let query = con.query('DELETE FROM records WHERE id = "'+req.body.id+'"', function (error, results, fields) {
        if (error) throw error;
        console.log('deleted ' + results.affectedRows + ' rows');
        res.send("deleted");
      })

    
        
  });
  
app.listen(port, () => {  console.log( port);});
