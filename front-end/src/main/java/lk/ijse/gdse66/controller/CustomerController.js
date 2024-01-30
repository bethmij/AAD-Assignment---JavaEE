import {setCusID} from "./OrderController.js";
import {customer} from "../model/Customer.js";

let cusId = $("#txtCusID");
let cusName = $("#txtCusName");
let cusAddress = $("#txtCusAddress");
let cusSalary = $("#txtCusSalary");
let btnCustomerSave = $('#btnSave');



$(document).on('keydown', function(event) {
    if (event.keyCode === 9) {
        event.preventDefault();
    }
});

getAll();

btnCustomerSave.click(function (event){
    let newCustomer = Object.assign({}, customer);
    newCustomer.cusID = cusId.val();
    newCustomer.cusName = cusName.val();
    newCustomer.cusAddress = cusAddress.val();
    newCustomer.cusSalary = cusSalary.val();

    if(btnCustomerSave.text()==="Save ") {
        event.preventDefault();
        const userChoice = window.confirm("Do you want to save the customer?")
        if (userChoice) {
            getCusIDList( function (IDList) {
                if (!(IDList.includes(cusId.val()))) {
                    $.ajax({
                        url: "http://localhost:8000/java-pos/customer",
                        method: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(newCustomer),
                        success: function (resp, status, xhr) {
                            if (xhr.status === 200) {
                                alert(resp)
                                getAll();
                                deleteDetail();
                                setFeilds();
                                clearAll(event);
                                getCusIDList(function (IDList) {
                                   setCusID(IDList);
                                });
                                btnCustomerSave.attr("disabled", true);
                            }
                        },
                        error: function (xhr, status, error) {
                            alert("Error : "+xhr.responseText)
                        }
                    });
                } else {
                    alert("Duplicate customer ID!");
                }
            });
        }

    }else if(btnCustomerSave.text()==="Update ") {
        const userChoice = window.confirm("Do you want to update the customer?");
        if (userChoice) {
            let newCustomer = Object.assign({}, customer);
            newCustomer.cusID = cusId.val();
            newCustomer.cusName = cusName.val();
            newCustomer.cusAddress = cusAddress.val();
            newCustomer.cusSalary = cusSalary.val();

            $.ajax({
                url: "http://localhost:8000/java-pos/customer",
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(newCustomer),
                success: function (resp, status, xhr) {
                    if (xhr.status === 200) {
                        alert(resp)
                        getAll();
                        clearAll(event);
                        btnCustomerSave.text("Save ");
                        btnCustomerSave.attr("disabled", true);
                        cusId.attr("disabled", false);
                    }
                },
                error: function (xhr) {
                    alert("Error : "+xhr.responseText)
                }
            });
        }
    }
    event.preventDefault();
})

$('#clear').click(function (event){
    clearAll(event);
})

function clearAll(event) {
    cusId.val("");
    cusName.val("");
    cusAddress.val("");
    cusSalary.val("");
    $('#txtCusID').css("border","1px solid white");
    $('#cusIDPara').text("");
    $('#txtCusName').css("border","1px solid white");
    $('#cusNamePara').text("");
    $('#txtCusAddress').css("border","1px solid white");
    $('#cusAddressPara').text("");
    $('#txtCusSalary').css("border","1px solid white");
    $('#cusSalaryPara').text("");
    btnCustomerSave.text("Save ");
    btnCustomerSave.attr("disabled", true);
    event.preventDefault();
    cusId.attr("disabled", false);
}



$('#getAll').click(function (){
    getAll();
})

function getAll() {

    $.ajax({
        url: "http://localhost:8000/java-pos/customer?option=GET",
        method: "GET",
        success: function (resp, status, xhr) {

            if(xhr.status===200) {
                let cusBody = $("#cusTBody");
                cusBody.empty();
                for (let customer of resp) {
                    cusBody.append(`
                        <tr>
                            <th scope="row">${customer.cusID}</th>
                            <td>${customer.cusName}</td>
                            <td>${customer.cusAddress}</td>
                            <td>${customer.cusSalary}</td>
                            <td style="width: 10%"><img  class="delete"  src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" style="opacity: 100%;" "></td>
                        </tr>`);
                    deleteDetail();
                    setFeilds();
                }
            }
        },
        error: function (xhr){
            console.log("Error : ", xhr.statusText);
        }
    })
}

setFeilds();

function setFeilds() {
    $('#cusTBody>tr').click(function () {
        cusId.val($(this).children(':eq(0)').text());
        cusName.val($(this).children(':eq(1)').text());
        cusAddress.val($(this).children(':eq(2)').text());
        cusSalary.val($(this).children(':eq(3)').text());
        btnCustomerSave.text("Update ");
        btnCustomerSave.attr("disabled", false);
        cusId.attr("disabled", true);
    })
}

deleteDetail();

function deleteDetail() {
    let btnDelete = $('.delete');
    btnDelete.on("mouseover", function () {
            $(this).css("cursor", "pointer")
        }
    )

    btnDelete.click(function (event) {
        const userChoice = window.confirm("Do you want to delete the customer?");

        if (userChoice) {
            let deleteRow = $(this).parents('tr');
            let cusID = $( deleteRow.children(':nth-child(1)')).text();

            $.ajax({
                url: "http://localhost:8000/java-pos/customer?cusID="+cusID,
                method: "DELETE",
                success: function (resp, status, xhr){
                    if(xhr.status === 200) {
                        alert(resp);
                        deleteRow.remove();
                        clearAll(event);
                        getCusIDList(function (IDList) {
                            setCusID(IDList)
                        });
                    }
                },
                error: function (xhr) {
                    alert("Error : "+xhr.responseText)
                    console.log("Error : ", xhr.statusText);
                }
            });
            setCusID();
        }
    })
}

export function getCustomerList(id, callback) {
    $.ajax({
        url: "http://localhost:8000/java-pos/customer?option=SEARCH&cusID=" + id,
        method: "GET",
        success: function (resp, status, xhr) {
            callback(resp, xhr);
        },
        error: function (resp) {
            alert(resp);
        }
    });
}

$('#btnSearch').click(function (){
    let id = $('#txtSearch').val();
    let cusTBody = $('#cusTBody');

    if(id.length!==0) {
        getCusIDList( function (IDList) {
            if (IDList.includes(id)) {
                $.ajax({
                    url: "http://localhost:8000/java-pos/customer?option=SEARCH&cusID=" + id,
                    method: "GET",
                    success: function (resp) {
                        cusTBody.empty();
                        cusTBody.append(`
                                <tr>
                                   <th scope="row">${resp.cusID}</th>
                                   <td>${resp.cusName}</td>
                                   <td>${resp.cusAddress}</td>
                                   <td>${resp.cusSalary}</td>
                                   <td style="width: 10%"><img  class="delete"  src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                                </tr>`)

                        setFeilds();
                        deleteDetail();
                    },
                    error: function (xhr) {
                        alert("Error : "+xhr.responseText);
                    }
                })
            } else {
                alert("Invalid customer ID! Please try again")
            }
        });
    }else {
        alert("Please enter the customer ID!");
    }

});

export function getCusIDList(callback) {
    let cusIDList = [];
    $.ajax({
        url: "http://localhost:8000/java-pos/customer?option=ID",
        method: "GET",
        success: function (resp, status, xhr) {
            if(xhr.status === 200) {
                cusIDList = resp
                callback(cusIDList);
            }
        },
        error: function (xhr){
            alert("Error : "+xhr.responseText);
        }
    });
}

