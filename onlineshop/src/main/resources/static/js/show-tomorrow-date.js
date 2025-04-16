const daysBG = [
    'Неделя', 'Понеделник', 'Вторник', 'Сряда', 'Четвъртък', 'Петък', 'Събота'
];

const monthsBG = [
    'Януари', 'Февруари', 'Март', 'Април', 'Май', 'Юни',
    'Юли', 'Август', 'Септември', 'Октомври', 'Ноември', 'Декември'
];

const today = new Date();
const tomorrow = new Date(today);
tomorrow.setDate(today.getDate() + 1);

const day = tomorrow.getDate();
const month = monthsBG[tomorrow.getMonth()];
const weekday = daysBG[tomorrow.getDay()];

document.getElementById("tomorrow-info").textContent = `${day} ${month}, ${weekday}`;