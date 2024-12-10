import Validate from './validate.js';
const validator = new Validate();

let rValue;
const selectedValueSpan = document.getElementById("selected-value");

function handleClick(event) {
  console.log(event.target.value);
  rValue = event.target.value;
  selectedValueSpan.textContent = `Выбранное значение: ${rValue}`;
}
const buttons = document.querySelectorAll('.r-button');
buttons.forEach((button) => {
  button.addEventListener('click', handleClick);
});

document.getElementById('send-btn').addEventListener('click', function(event) {
    event.preventDefault();
    const x = document.querySelector('input[name="x"]:checked');
    const y = document.querySelector('#coord-y');
    const r = parseFloat(rValue);


    const check = validator.check(x, y, r);
    if (check.allOk){
        const coords = validator.getCoords();
        fetch(`http://localhost:8080/fcgi-bin/server.jar`, {
            method: 'POST',
            body: `x=${coords.x}&y=${coords.y}&r=${coords.r}`,
          })
          .then(response => {
            if (!response.ok) {
              throw new Error(`${response.status}`);
            }
            return response.text();
          })
          .then(function(answer){
            localStorage.setItem("session", answer);
            var res = JSON.parse(answer);
            var table = document.getElementById("res-table"),
            tbody = table.getElementsByTagName("tbody")[0];
            var row = document.createElement("tr");
            var isHit = document.createElement("td");
            var x = document.createElement("td");
            var y = document.createElement("td");
            var r = document.createElement("td");
            var time = document.createElement("td");
            var workTime = document.createElement("td");
            if (res.error === "all ok") {
                document.getElementById("input-log").innerText = '';
                if (res.hit === "true"){
                  isHit.innerText = "Попал";
                }
                else {
                  isHit.innerText = "Не туда";
                }
                x.innerText = res.x;
                y.innerText = res.y;
                r.innerText = res.r;
                var timeTemp = Number(res.time);
                time.innerText = (timeTemp).toFixed(6);
                workTime.innerText = res.workTime;
                row.appendChild(isHit);
                row.appendChild(x);
                row.appendChild(y);
                row.appendChild(r);
                row.appendChild(time);
                row.appendChild(workTime);
                tbody.appendChild(row);

                document.getElementById("dot").setAttribute("cx", String(300 + Number(res.x) * (200 / Number(res.r))));
                document.getElementById("dot").setAttribute("cy", String(300 - Number(res.y) * (200 / Number(res.r))));
                document.getElementById("dot").setAttribute("fill-opacity", "1");
                document.getElementById("dot").setAttribute("stroke-opacity", "0.5");
            } else{
              if (res.error === "fill"){
                document.getElementById("input-log").innerText = "Заполните все поля";
                document.getElementById("dot").setAttribute("fill-opacity", "0");
                document.getElementById("dot").setAttribute("stroke-opacity", "0");}

              else if (res.error === "method"){
                document.getElementById("input-log").innerText = "Только POST запросы";
                document.getElementById("dot").setAttribute("fill-opacity", "0");
                document.getElementById("dot").setAttribute("stroke-opacity", "0");
              }
                
            }
        })
          .catch(error => {
            console.error('Ошибка:', error);
          })
    }else{
        document.getElementById("input-log").innerText = check.log;
        document.getElementById("dot").setAttribute("fill-opacity", "0");
        document.getElementById("dot").setAttribute("stroke-opacity", "0"); 
    }
}
);
