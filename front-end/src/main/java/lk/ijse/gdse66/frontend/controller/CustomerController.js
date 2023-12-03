import {customer} from "./model/Customer.js";
import {customerDetail, itemDetail} from "./db/DB.js";
import {setCusID} from "./OrderController.js";

let cusId = $('#txtCusID');
let cusName = $('#txtCusName');
let cusAddress = $('#txtCusAddress');
let cusSalary = $('#txtCusSalary');
let btnCustomerSave = $('#btnSave');

$(document).on('keydown', function(event) {
    if (event.keyCode === 9) {
        event.preventDefault();
    }
});

btnCustomerSave.click(function (event){
    // let customer = {"cusID":cusId.val(), "cusName":cusName.val(), "cusAddress":cusAddress.val(), "cusSalary":cusSalary.val()}
    let customer = $("#cusForm").serialize()
    console.log(customer)

    $.ajax({
        url: "http://localhost:8080/java-pos/customer",
        method: "POST",
        data: customer,
        success: function (resp){
            if(resp.status===200){
                alert(resp.message);
                $('#cusTBody').append(
                    `<tr>
                        <th scope="row">${cusId.val()}</th>
                        <td>${cusName.val()}</td>
                        <td>${cusAddress.val()}</td>
                        <td>${cusSalary.val()}</td>
<!--                        <td style="width: 10%"><img class="delete opacity-75" src="../../../../../../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" ></td>-->
                    </tr>`
                );
            }else {
                alert(resp.message);
            }
        },
        error: function (resp){
            alert(resp.message);
        }
    }),
    // if(btnCustomerSave.text()=="Save ") {
    //     let count = 0;
    //     var userChoice = window.confirm("Do you want to save the customer?");
    //
    //     if (userChoice) {
    //         for (let i = 0; i < customerDetail.length; i++) {
    //             if(customerDetail[i].id!=cusId.val()) {
    //                 count++;
    //             }
    //         }
    //         if(count==customerDetail.length) {
    //         let newCustomerDetails = Object.assign({}, customer);
    //             newCustomerDetails.id = cusId.val();
    //             newCustomerDetails.name = cusName.val();
    //             newCustomerDetails.address = cusAddress.val();
    //             newCustomerDetails.salary = cusSalary.val();
    //
    //             customerDetail.push(newCustomerDetails);
    //
    //             $('#cusTBody').append(
    //                 `<tr>
    //                     <th scope="row">${cusId.val()}</th>
    //                     <td>${cusName.val()}</td>
    //                     <td>${cusAddress.val()}</td>
    //                     <td>${cusSalary.val()}</td>
    //                     <td style="width: 10%"><img class="delete" src="../../CSS_Framework/POS/assets/icons8-delete-96.png" alt="Logo" width="50%" className="opacity-75"></td>
    //                 </tr>`
    //             );
    //             deleteDetail();
    //             setFeilds();
    //             clearAll(event);
    //             setCusID();
    //             btnCustomerSave.attr("disabled", true);
    //         }else {
    //             alert("Duplicate customer ID!");
    //         }
    //     }
    // }else if(btnCustomerSave.text()=="Update ") {
    //     for (let i = 0; i < customerDetail.length; i++) {
    //
    //         if(customerDetail[i].id == $('#txtCusID').val()){
    //             customerDetail[i].name = $('#txtCusName').val();
    //             customerDetail[i].address = $('#txtCusAddress').val();
    //             customerDetail[i].salary = $('#txtCusSalary').val();
    //             getAll();
    //             clearAll(event);
    //             btnCustomerSave.text("Save ");
    //             btnCustomerSave.attr("disabled", true);
    //             cusId.attr("disabled", false);
    //         }
    //     }
    // }
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
    $('#txtCusName').css("border","1px solid white");;
    $('#cusNamePara').text("");
    $('#txtCusAddress').css("border","1px solid white");;
    $('#cusAddressPara').text("");
    $('#txtCusSalary').css("border","1px solid white");;
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
        url: "http://localhost:8080/java-pos/customer",
        method: "GET",
        success: function (resp) {
            if(resp.status===200){
                for (let respElement of resp.data) {
                    tBody.append(`<tr>
                        <th scope="row">${respElement.cusID}</th>
                        <td>${respElement.cusName}</td>
                        <td>${respElement.cusAddress}</td>
                        <td>${respElement.cusSalary}</td>
<!--                        <td style="width: 10%"><img class="delete opacity-75" src="../../../../../../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" ></td>-->
                </tr>`);
                }

            }
        }
    })
    let tBody = $('#cusTBody')
    tBody.empty();

    for (let i = 0; i < customerDetail.length; i++) {
        tBody.append(`<tr>
            <th scope="row">${customerDetail[i].id}</th>
            <td>${customerDetail[i].name}</td>
            <td>${customerDetail[i].address}</td>
            <td>${customerDetail[i].salary}</td>
<!--            <td style="width: 10%"><img class="delete" src="../../../../../../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" className="opacity-75"></td>-->
            </tr>`);
        deleteDetail();
        setFeilds();
    }
    ;
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
    btnDelete.on("mouseover", function (){
        $(this).css("cursor", "pointer");}
    )

    btnDelete.click(function () {
        var userChoice = window.confirm("Do you want to delete the customer?");

        if (userChoice) {
            $(this).parents('tr').remove();
            let tableCode = $(this).parents('tr').children(':nth-child(1)');

            for (let i = 0; i < customerDetail.length; i++) {
                if($(tableCode[0]).text() ==customerDetail[i].id){
                    customerDetail.splice(i,1);
                    console.log(customerDetail);
                }
            }
            setCusID();
        }
    })
}

$('#btnSearch').click(function (){
    let id = $('#txtSearch').val();
    let tbody = $('#cusTBody');
    let count = 0;

    if(id.length!=0) {
        for (let i = 0; i < customerDetail.length; i++) {
            if (customerDetail[i].id == id) {
                count++;
                tbody.empty();

                tbody.append(`<tr>
                <th scope="row">${customerDetail[i].id}</th>
                <td>${customerDetail[i].name}</td>
                <td>${customerDetail[i].address}</td>
                <td>${customerDetail[i].salary}</td>
<!--                <td style="width: 10%"><img class="delete" src="../../../../../../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" className="opacity-75"></td>-->
                </tr>`);
                deleteDetail();
                setFeilds();
            }
        }
        if (count != 1) {
            alert("No such Customer..please check the ID");
        }
    }else {
        alert("Please enter the ID");
    }
})

