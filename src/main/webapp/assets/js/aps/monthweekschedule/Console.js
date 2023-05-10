window.onload = function() {
	const xhr = new XMLHttpRequest();
	xhr.open('GET', 'http://localhost:8081/admin/scheduproductplanmonth/getApsWeekscheduleList')
	xhr.send();
	xhr.onreadystatechange = function() {
		// if(xhr.readyState === 4) {
		// 	if(xhr.status >= 200 && xhr.status < 300) {
		// 		// console.log(xhr.status)
		// 		// console.log(xhr.statusText)
		// 		// console.log(xhr.getAllResponseHeaders());
		// 		// console.log(xhr.response)
		// 	}
		// }
	}
}

function reday() {
	var table = document.getElementsByTagName("table")[0];
	var cells = table.getElementsByTagName("td");
	for (let i = 1; i < cells.length; i++) {
		let cell = cells[i];
		cell.onclick = function() {
			cell.innerHTML = `<input class="inp" onblur="editBlur()"/>`
		}
	}
}
reday();

function editBlur() {
	var table = document.getElementsByTagName("table")[0];
	var cells = table.getElementsByTagName("td");
	for (let i = 1; i < cells.length; i++) {
		let cell = cells[i];
		cell.onclick = function() {
			cell.innerHTML = `<td></td>`
		}
	}
}
