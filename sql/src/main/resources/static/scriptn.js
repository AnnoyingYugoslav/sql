function getklasy() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch('/api/get-klasy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        const klasyContainer = document.getElementById('klasy');
        if (responseData[1]) {
            for (let key in responseData) {
                if (key !== '1' && responseData.hasOwnProperty(key)) {
                    const klasa = responseData[key];
                    const przycisk = document.createElement('button');
                    przycisk.textContent = `${klasa.nazwa}`;
                    przycisk.dataset.klasaId = klasa.id;
                    przycisk.addEventListener('click', function() {
                        const klasaId = przycisk.dataset.klasaId;
                        console.log(`Wybrano klasę: ${klasa.nazwa} ${klasa.id}`);
                        //getsprawdzianyn(klasaId);
                        getuczniowie(klasaId);
                    });
                    klasyContainer.appendChild(przycisk);
                }
            }
        } else {
            klasyContainer.textContent = 'Brak klas';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function getsprawdzianyn(id) {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch(`/api/get-sprawdzian-nauczyciel/${id}`, {
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
        console.log("2:", responseData[2]);
        console.log("3:", responseData[3]);
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
function getuczniowie(id) {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const IdValue = id;
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: IdValue
    };

    fetch(`/api/get-uczniowie`, {
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
        console.log("2:", responseData[2]);
        console.log("3:", responseData[3]);
        const uczenContainer = document.getElementById('uczniowie');
        uczenContainer.innerHTML = '';
        if (responseData[1]) {
            for (let i = 2; responseData[i] !== undefined; i++) {
                const uczen = responseData[i];
                const uczenElement = document.createElement('p');
                uczenElement.textContent = `${uczen.imie}, ${uczen.nazwisko}`;
                uczenContainer.appendChild(uczenElement);
            }
        } else {
            uczenContainer.textContent = 'Brak uczniów';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}