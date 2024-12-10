export default class Validate
{
    x;
    y;
    r;
    constructor() {
        this.log = "";
    }
    getCoords(){
        return{
            x: this.x,
            y: this.y,
            r: this.r,
        };
    }
    check(x, y, r){
        this.log = "";
        if (!this.checkForNull(x, y, r))
            return {
                allOk: false,
                log: "Заполните все поля"
            };
        return{
            allOk: this.checkX(x) && this.checkY(y) && this.checkR(r),
            log: this.log
        };
    }

    checkX(x){
        x = parseFloat(x.value);
        this.x = x;
        if ([-3, -2, -1, 0, 1, 2, 3, 4, 5].includes(x)){
            this.log = "";
            return true;
        }
        else {
            this.log = "X нужно выбрать";
            return false;
        }
    }

    checkY(y){
        if (!/^(-?\d+(\.\d+)?)$/.test(y.value)){
            this.log = "Y должен содержать число";
            return false;
        }
        if (y.value ==""){
            this.log = "Y должен содержать число";
            return false;
        }
        y = parseFloat(y.value);
        this.y = y;
        if (isNaN(y)) {
            this.log = "Y должен содержать число";
            return false;
        }
        if (-3 <= y && y <= 5){
            this.log = "";
            return true;
        }
        else {
            this.log = "Y должен содержать число";
            return false;
        }
    }

    checkR(r){
        this.r = r;
        if ([1, 1.5, 2, 2.5, 3].includes(r)){
            this.log = "";
            return true;
        }
        else {
            this.log = "R нужно выбрать";
            return false;
        }
    }

    checkForNull(x, y, r){
        if (x && y && r){
            return true;
        }else return false;
    }

}