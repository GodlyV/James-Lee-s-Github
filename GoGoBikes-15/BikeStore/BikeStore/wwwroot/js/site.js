// Please see documentation at https://docs.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.

// Write your JavaScript code.
function SelectRow(row) {
    var inputs = row.getElementsByTagName('input');
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].type == "checkbox") {
            inputs[i].checked = !inputs[i].checked;

            // Check if it's checked
            if (inputs[i].checked == true) {
                row.style.backgroundColor = "lightblue";
            }
            else {
                row.style.backgroundColor = "white";
            }

        }
    }
}

function UpdateHighlight() {
    var inputs = document.getElementsByTagName('input');
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].type == "checkbox") {
            // Check if it's checked
            if (inputs[i].checked == true) {
                inputs[i].style.backgroundColor = "lightblue";
            }
            else {
                inputs[i].style.backgroundColor = "white";
            }
        }
    }
}


