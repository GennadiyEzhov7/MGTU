

const url = 'http://localhost:3000/holidays'; 

const radio_m_gender = document.getElementById("m_gender");
const radio_w_gender= document.getElementById("w_gender");
const select_holiday = document.getElementById("select_holiday");
const submit_button = document.getElementById("submit_button");

radio_m_gender.addEventListener("change", async function() {
    select_holiday.disabled = false;
    radio_w_gender.checked = false;
    try{
        const response =  await fetch("/holidays", {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({gender: "m_gender"})
        })
        responseData = await response.json();
        const holidays = responseData.holidays;
        select_holiday.innerHTML = '';

        holidays.forEach(holiday => {
            const option = document.createElement('option');
            option.value = holiday;
            option.textContent = holiday;
            select_holiday.appendChild(option);
        });
        const submit_button = document.getElementById("submit_button");
        submit_button.disabled = false;
        console.log('Праздники:', holidays);

    } catch(error){
        console.log(error)
    }
    
});

radio_w_gender.addEventListener("change", async function() {
    select_holiday.disabled = false;
    radio_m_gender.checked = false;

    try{
        const response =  await fetch("/holidays", {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({gender: "w_gender"})
        })

        responseData = await response.json();
        const holidays = responseData.holidays;
        select_holiday.innerHTML = '';

        holidays.forEach(holiday => {
            const option = document.createElement('option');
            option.textContent = holiday;
            option.value = holiday;
            select_holiday.appendChild(option);
        });

        console.log('Праздники:', holidays);
        submit_button.disabled = false;
    } catch(error){
        console.log(error)
    }
});