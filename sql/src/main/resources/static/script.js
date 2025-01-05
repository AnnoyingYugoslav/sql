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
    if (sessionStorage.getItem('Nauczyciel') != "null"){
        document.location.href="nauczyciel.html";
    }
    else if (sessionStorage.getItem('Rodzic') != "null"){
        document.location.href="rodzic.html";
    }
    else{
        document.location.href="uczen.html";
    }
}

function logout() {
    sessionStorage.clear();
    document.location.href="index.html";
}
function chpswd() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = document.getElementById("Password").value;
    const Password2Value = document.getElementById("Password2").value;
    const info = document.getElementById("info");
    console.log(LoginValue);
    console.log(PasswordValue);
    console.log(Password2Value);
    if (LoginValue === '' || PasswordValue === ''){
        info.innerHTML = "Wypełnij wszystkie pola"
    }
    else{
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: Password2Value
    };

    fetch('/api/change-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        if (responseData[1]){
            sessionStorage.setItem("Password", Password2Value);
            if (sessionStorage.getItem('Nauczyciel') == true){
                document.location.href="nauczyciel.html?success=true";
            }
            else if (sessionStorage.getItem('Rodzic') == true){
                document.location.href="rodzic.html?success=true";
            }
            else{
                document.location.href="uczen.html?success=true";
            }
        }
        else{
            const infoElement = document.getElementById('info');
            document.getElementById("myForm").reset();
            infoElement.innerText = 'Podano złe hasło';
        }
    } 
    )
    .catch(error => {
        console.error('Error:', error);
    });
}
}

function getocenyu() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch('/api/get-oceny-uczen', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const ocenyContainer = document.getElementById('oceny');
        if (responseData[1]) {
            const groupedOceny = {};
            for (let i = 2; responseData[i] !== undefined; i++) {
                const ocena = responseData[i];
                const przedmiotNazwa = ocena.przedmiot.nazwa;
                if (!groupedOceny[przedmiotNazwa]) {
                    groupedOceny[przedmiotNazwa] = [];
                }
                groupedOceny[przedmiotNazwa].push(ocena);
            }
            for (const przedmiotNazwa in groupedOceny) {
                const przedmiotData = groupedOceny[przedmiotNazwa];
                const row = document.createElement('div');
                row.classList.add('przedmiot-row');
                const przedmiotDiv = document.createElement('div');
                przedmiotDiv.textContent = `${przedmiotNazwa}`;
                row.appendChild(przedmiotDiv);
                const ocenyDiv = document.createElement('div');
                przedmiotData.forEach(item => {
                    const ocenaDiv = document.createElement('span');
                    ocenaDiv.textContent = `${item.ocena} `;
                    ocenyDiv.appendChild(ocenaDiv);
                });
                row.appendChild(ocenyDiv);
                ocenyContainer.appendChild(row);
            }
        } else {
            ocenyContainer.textContent = 'Brak ocen';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function getsprawdzianyu() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch('/api/get-sprawdziany-uczen', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const sprawdzianyContainer = document.getElementById('sprawdziany');
        if (responseData[1]) {
            for (let i = 2; responseData[i] !== undefined; i++) {
                const sprawdzian = responseData[i];
                const sprawdzianElement = document.createElement('p');
                sprawdzianElement.textContent = `${sprawdzian.kategoria}, ${sprawdzian.przedmiot.nazwa} w sali ${sprawdzian.sala.nazwa} z ${sprawdzian.nauczyciel.imie} ${sprawdzian.nauczyciel.nazwisko}`;
                sprawdzianyContainer.appendChild(sprawdzianElement);
            }
        } else {
            sprwadzianyContainer.textContent = 'Brak sprawdzianów';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function getlekcjeu() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch('/api/get-lekcja-uczen', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const lekcjaContainer = document.getElementById('lekcje');
        console.log("Received data:");
        console.log("1:", responseData[1]);
        console.log("2:", responseData[2]);
        if (responseData[1]) {
            // for (let i = 2; responseData[i] !== undefined; i++) {
            //     const lekcja = responseData[i];
            //     const lekcjaElement = document.createElement('p');
            //     lekcjaElement.textContent = `Sprawdzian, ${sprawdzian.przedmiot.nazwa} w sali ${sprawdzian.sala.nazwa} z ${sprawdzian.nauczyciel.imie} ${sprawdzian.nauczyciel.nazwisko}`;
            //     lekcjaContainer.appendChild(lekcjaElement);
            // }
        } else {
            const noTestsMessage = document.createElement('p');
            noTestsMessage.textContent = 'Brak sprawdzianów';
            lekcjaContainer.appendChild(noTestsMessage);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function getuwagiu() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch('/api/get-uwagi-uczen', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const uwagaContainer = document.getElementById('uwaga');
        console.log("Received data:");
        console.log("1:", responseData[1]);
        console.log("2:", responseData[2]);
        if (responseData[1]) {
            // for (let i = 2; responseData[i] !== undefined; i++) {
            //     const lekcja = responseData[i];
            //     const lekcjaElement = document.createElement('p');
            //     lekcjaElement.textContent = `Sprawdzian, ${sprawdzian.przedmiot.nazwa} w sali ${sprawdzian.sala.nazwa} z ${sprawdzian.nauczyciel.imie} ${sprawdzian.nauczyciel.nazwisko}`;
            //     lekcjaContainer.appendChild(lekcjaElement);
            // }
        } else {
            const noTestsMessage = document.createElement('p');
            noTestsMessage.textContent = 'Brak sprawdzianów';
            uwagaContainer.appendChild(noTestsMessage);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function rodzicu() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch('/api/get-uczen-rodzica', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        if (responseData[1]) {
            const dzieciContainer = document.getElementById('dzieci');
            for (let key in responseData) {
                if (key !== '1' && responseData.hasOwnProperty(key)) {
                    const dziecko = responseData[key];
                    const przycisk = document.createElement('button');
                    przycisk.textContent = `${dziecko.imie} ${dziecko.nazwisko}`;
                    przycisk.dataset.dzieckoId = dziecko.id;
                    przycisk.addEventListener('click', function() {
                        const dzieckoId = przycisk.dataset.dzieckoId;
                        console.log(`Wybrano dziecko: ${dziecko.imie} ${dziecko.nazwisko}, id: ${dzieckoId}`);
                        getocenyr(dzieckoId);
                        getsprawdzianyr(dzieckoId);
                    });
                    dzieciContainer.appendChild(przycisk);
                }
            }
        } else {
            console.log("Brak dzieci");
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function getocenyr(id) {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch(`/api/get-oceny-rodzic/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const ocenyContainer = document.getElementById('oceny');
        ocenyContainer.innerHTML = '';
        if (responseData[1]) {
            const groupedOceny = {};
            for (let i = 2; responseData[i] !== undefined; i++) {
                const ocena = responseData[i];
                const przedmiotNazwa = ocena.przedmiot.nazwa;
                if (!groupedOceny[przedmiotNazwa]) {
                    groupedOceny[przedmiotNazwa] = [];
                }
                groupedOceny[przedmiotNazwa].push(ocena);
            }
            for (const przedmiotNazwa in groupedOceny) {
                const przedmiotData = groupedOceny[przedmiotNazwa];
                const row = document.createElement('div');
                row.classList.add('przedmiot-row');
                const przedmiotDiv = document.createElement('div');
                przedmiotDiv.textContent = `${przedmiotNazwa}`;
                row.appendChild(przedmiotDiv);
                const ocenyDiv = document.createElement('div');
                przedmiotData.forEach(item => {
                    const ocenaDiv = document.createElement('span');
                    ocenaDiv.textContent = `${item.ocena} `;
                    ocenyDiv.appendChild(ocenaDiv);
                });
                row.appendChild(ocenyDiv);
                ocenyContainer.appendChild(row);
            }
        } else {
            ocenyContainer.textContent = 'Brak ocen';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function getsprawdzianyr(id) {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch(`/api/get-sprawdzian-rodzic/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const sprawdzianyContainer = document.getElementById('sprawdziany');
        sprawdzianyContainer.innerHTML = '';
        if (responseData[1]) {
            for (let i = 2; responseData[i] !== undefined; i++) {
                const sprawdzian = responseData[i];
                const sprawdzianElement = document.createElement('p');
                sprawdzianElement.textContent = `${sprawdzian.kategoria}, ${sprawdzian.przedmiot.nazwa} w sali ${sprawdzian.sala.nazwa} z ${sprawdzian.nauczyciel.imie} ${sprawdzian.nauczyciel.nazwisko}`;
                sprawdzianyContainer.appendChild(sprawdzianElement);
            }
        } else {
            sprawdzianyContainer.textContent = 'Brak sprawdzianów';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
//Lekcje i uwagi analogicznie do ucznia, dodać jak będą usunięte błędy


const urlParams = new URLSearchParams(window.location.search);

if (urlParams.has('success')) {
    const infoElement = document.getElementById('info');
    infoElement.innerText = 'Hasło zostało zmienione';
}