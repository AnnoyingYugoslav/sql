function Logina() {
    // Get the field values
    const LoginValue = document.getElementById("Login").value;
    const PasswordValue = document.getElementById("Password").value;

    // Create a JavaScript object with the field values
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    // Send the data to the backend
    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        // Log the received data to the console
        console.log("Received data:");
        console.log("1:", responseData[1]);
        console.log("2:", responseData[2]);
        console.log("3:", responseData[3]);
        if (responseData[1]){
            sessionStorage.setItem("Login", LoginValue);
            sessionStorage.setItem("Password", PasswordValue);
            sessionStorage.setItem("Nauczyciel", responseData[2]);
            sessionStorage.setItem("Rodzic", responseData[3]);
            sessionStorage.setItem("Uczen", responseData[4]);
            returnp();
        }
        else{
            const infoElement = document.getElementById('info');
            infoElement.innerText = 'There is no such account';
            document.getElementById("myForm").reset();
        } 
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function returnp(){
    if (sessionStorage.getItem('Nauczyciel') == true){
        document.location.href="nauczyciel.html";
    }
    else if (sessionStorage.getItem('Rodzic') == true){
        document.location.href="rodzic.html";
    }
    else{
        document.location.href="uczen.html";
    }
}

function Klasy() {
    // Get the field values
    const LoginValue = document.getElementById("Login").value;
    const PasswordValue = document.getElementById("Password").value;

    // Create a JavaScript object with the field values
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    // Send the data to the backend
    fetch('/api/get-klasy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        // Log the received data to the console
        console.log("Received data:");
        console.log("1:", responseData[1]);
        console.log("2:", responseData[2]);
        console.log("3:", responseData[3]);
        console.log("4:", responseData[4]);
        // if (responseData[1]){
        //     sessionStorage.setItem("Login", LoginValue);
        //     sessionStorage.setItem("Password", PasswordValue);
        //     sessionStorage.setItem("Nauczyciel", responseData[2]);
        //     sessionStorage.setItem("Rodzic", responseData[3]);
        //     sessionStorage.setItem("Rodzic", responseData[4]);
        //     sessionStorage.setItem("Uczen", responseData[1]);
        // }
        // else{
        //     const infoElement = document.getElementById('info');
        //     infoElement.innerText = 'Error';
        //     document.getElementById("myForm").reset();
        // } 
    })
    .catch(error => {
        console.error('Error:', error);
    });
}