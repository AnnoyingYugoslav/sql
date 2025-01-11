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
                        sessionStorage.setItem("KlasaId", klasa.id);
                        sessionStorage.setItem("KlasaNazwa", klasa.nazwa);
                        getsprawdzianyn(klasaId);
                        getuczniowie(klasaId);
                        getlekcjen(klasaId);
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
        const sprawdzianyContainer = document.getElementById('sprawdziany');
        sprawdzianyContainer.innerHTML = '';
        if (responseData[1]) {
            for (let i = 2; responseData[i] !== undefined; i++) {
                const sprawdzian = responseData[i];
                const sprawdzianElement = document.createElement('p');
                const link = document.createElement('a');
                link.href = 'editsprawdzian.html';
                const Minutes = sprawdzian.godzina.m.toString().padStart(2, '0');
                const Hours = sprawdzian.godzina.h.toString().padStart(2, '0');
                const Days = sprawdzian.dzien.dzien.toString().padStart(2, '0');
                const Months = sprawdzian.dzien.miesiac.toString().padStart(2, '0');
                link.textContent = `${sprawdzian.dzien.dzien}-${sprawdzian.dzien.miesiac}-${sprawdzian.dzien.rok} o godzinie ${Hours}:${Minutes} ${sprawdzian.kategoria}, ${sprawdzian.przedmiot.nazwa} w sali ${sprawdzian.sala.nazwa} z ${sprawdzian.nauczyciel.imie} ${sprawdzian.nauczyciel.nazwisko}`;
                link.onclick = function () {
                    sessionStorage.setItem("SprawdzianId", sprawdzian.id);
                    sessionStorage.setItem("SprawdzianKat", sprawdzian.kategoria);
                    sessionStorage.setItem("SprawdzianGodz", Hours);
                    sessionStorage.setItem("SprawdzianMin", Minutes);
                    sessionStorage.setItem("SprawdzianDzien", Days);
                    sessionStorage.setItem("SprawdzianMiesiac", Months);
                    sessionStorage.setItem("SprawdzianRok", sprawdzian.dzien.rok);
                    sessionStorage.setItem("SprawdzianSala", sprawdzian.sala.id);
                    sessionStorage.setItem("SprawdzianPrzedmiot", sprawdzian.przedmiot.id);
                };
                sprawdzianElement.appendChild(link);
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
        const uczenContainer = document.getElementById('uczniowie');
        uczenContainer.innerHTML = '';
        if (responseData[1]) {
            for (let i = 2; responseData[i] !== undefined; i++) {
                const uczen = responseData[i];
                const uczenElement = document.createElement('div');
                const uczenText = document.createElement('span');
                uczenText.textContent = `${uczen.imie} ${uczen.nazwisko}`;
                uczenText.style.cursor = "pointer";
                uczenText.addEventListener('click', () => {
                    sessionStorage.setItem('UczenId', uczen.id);
                    window.location.href = 'uczenlookup.html';
                });
                uczenElement.appendChild(uczenText);
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
function getlekcjen(id) {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch(`/api/get-lekcja-nauczyciel/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const lekcjaContainer = document.getElementById('lekcje');
        lekcjaContainer.innerHTML = '';
        console.log("Received data:");
        console.log("1:", responseData[1]);
        console.log("2:", responseData[2]);

        if (responseData[1]) {
            const lekcjeGroupedByDay = {};

            for (let i = 2; responseData[i] !== undefined; i++) {
                const lekcja = responseData[i];
                const dzienTygodnia = lekcja.dzTyg;

                if (!lekcjeGroupedByDay[dzienTygodnia]) {
                    lekcjeGroupedByDay[dzienTygodnia] = [];
                }

                lekcjeGroupedByDay[dzienTygodnia].push(lekcja);
            }

            const dniTygodnia = ["poniedziałek", "wtorek", "środa", "czwartek", "piątek"];
            dniTygodnia.forEach(dzien => {
                const lekcjeWDniu = lekcjeGroupedByDay[dzien];
                if (lekcjeWDniu) {
                    const dzienElement = document.createElement('h3');
                    dzienElement.textContent = dzien.charAt(0).toUpperCase() + dzien.slice(1);
                    lekcjaContainer.appendChild(dzienElement);
                    lekcjeWDniu.forEach(lekcja => {
                        const lekcjaElement = document.createElement('p');
                        const startMinutes = lekcja.poczgodz.m.toString().padStart(2, '0');
                        const endMinutes = lekcja.kongodz.m.toString().padStart(2, '0');
                        lekcjaElement.textContent = `${lekcja.poczgodz.h}:${startMinutes} - ${lekcja.kongodz.h}:${endMinutes} ${lekcja.przedmiot.nazwa} w sali ${lekcja.sala.nazwa}`;
                        lekcjaContainer.appendChild(lekcjaElement);
                    });
                }
            });
        } else {
            lekcjaContainer.textContent = 'Brak lekcji';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function getocenyn() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const id = sessionStorage.getItem("UczenId");
    const data = {
        1: LoginValue,
        2: PasswordValue,
    };

    fetch(`/api/get-oceny-nauczyciel/${id}`, {
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
                    const ocenaLink = document.createElement('a');
                    ocenaLink.textContent = `${item.ocena} `;
                    ocenaLink.href = `editocena.html`;
                    ocenaLink.classList.add('ocena-link');
                    ocenaLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        sessionStorage.setItem('OcenaId', item.id);
                        sessionStorage.setItem('Ocena', item.ocena);
                        window.location.href = ocenaLink.href;
                    });
                    ocenaDiv.appendChild(ocenaLink);
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

function getuwagin() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem("Password");
    const id = sessionStorage.getItem("UczenId");
    const data = {
        1: LoginValue,
        2: PasswordValue
    };

    fetch(`/api/get-uwagi-nauczyciel/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        const uwagaContainer = document.getElementById('uwagi');
        uwagaContainer.innerHTML = '';
        if (responseData[1]) {
             for (let i = 2; responseData[i] !== undefined; i++) {
                const uwaga = responseData[i];
                const uwagaElement = document.createElement('p');
                const link = document.createElement('a');
                link.href = 'edituwaga.html';
                link.textContent = `${uwaga.nauczyciel.imie} ${uwaga.nauczyciel.nazwisko} dnia ${uwaga.dzien.dzien}-${uwaga.dzien.miesiac}-${uwaga.dzien.rok} : ${uwaga.tresc}`;
                link.onclick = function () {
                    sessionStorage.setItem("UwagaId", uwaga.id);
                    sessionStorage.setItem("UwagaOpis", uwaga.tresc);
                };

                uwagaElement.appendChild(link);
                uwagaContainer.appendChild(uwagaElement);
             }
        } else {
            uwagaContainer.textContent = 'Brak uwag';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function addocena() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("UczenId");
    const Grade = document.getElementById('grade').value;
    const Subject = document.getElementById('subject').value;
    const description = document.getElementById('description').value;
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: id,
        4: Grade,
        5: Subject,
        6: description
    };
    console.log(data);
    fetch('/api/add-ocena', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("0:", responseData[0]);
        document.location.href="uczenlookup.html?success2=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function adduwaga() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("UczenId");
    const description = document.getElementById('description').value;
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: id,
        4: description
    };
    console.log(data);
    fetch('/api/add-uwaga', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1", responseData[1]);
        document.location.href="uczenlookup.html?success3=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function editocena() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("OcenaId");
    const Grade = document.getElementById('ocena').value;
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: Grade
    };
    console.log(data);
    fetch(`/api/edit-ocena/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        document.location.href="uczenlookup.html?success4=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function removeocena() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("OcenaId");
    const data = {
        1: LoginValue,
        2: PasswordValue,
    };
    console.log(data);
    fetch(`/api/remove-ocena/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        document.location.href="uczenlookup.html?success5=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function edituwaga() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("UwagaId");
    const Description = document.getElementById('uwaga').value;
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: id,
        4: Description
    };
    console.log(data);
    fetch(`/api/edit-uwaga/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        document.location.href="uczenlookup.html?success6=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function removeocena() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("OcenaId");
    const data = {
        1: LoginValue,
        2: PasswordValue,
    };
    console.log(data);
    fetch(`/api/remove-ocena/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        document.location.href="uczenlookup.html?success6=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function removeuwaga() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("UwagaId");
    const data = {
        1: LoginValue,
        2: PasswordValue,
    };
    console.log(data);
    fetch(`/api/remove-uwaga/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        document.location.href="uczenlookup.html?success7=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function addsprawdzian() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("KlasaId");
    const Subject = document.getElementById('subject').value;
    const Day = document.getElementById('day').value;
    const Room = document.getElementById('sala').value;
    const description = document.getElementById('description').value;
    const Time = document.getElementById('time').value;
    const [year, month, day] = Day.split('-');
    const formattedDate = `${day}.${month}.${year}`; 
    console.log(formattedDate);
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: id,
        4: Subject,
        5: formattedDate,
        6: Room,
        7: description,
        8: Time
    };
    console.log(data);
    console.log(data);
    fetch('/api/add-sprawdzian', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[0]);
        document.location.href="nauczyciel.html?success8=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function editsprawdzian() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("KlasaId");
    const Subject = document.getElementById('subject').value;
    const Day = document.getElementById('day').value;
    const Room = document.getElementById('sala').value;
    const description = document.getElementById('description').value;
    const Time = document.getElementById('time').value;
    const [year, month, day] = Day.split('-');
    const formattedDate = `${day}.${month}.${year}`; 
    console.log(formattedDate);
    const data = {
        1: LoginValue,
        2: PasswordValue,
        3: id,
        4: Subject,
        5: formattedDate,
        6: Room,
        7: description,
        8: Time
    };
    console.log(data);
    fetch(`/api/edit-sprawdzian/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        document.location.href="nauczyciel.html?success9=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function removesprawdzian() {
    const LoginValue = sessionStorage.getItem('Login');
    const PasswordValue = sessionStorage.getItem('Password');
    const id = sessionStorage.getItem("SprawdzianId");
    const data = {
        1: LoginValue,
        2: PasswordValue,
    };
    console.log(data);
    fetch(`/api/remove-sprawdzian/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(responseData => {
        console.log("Received data:");
        console.log("1:", responseData[1]);
        document.location.href="nauczyciel.html?success10=true";
    })
    .catch(error => {
        console.error('Error:', error);
    });
}