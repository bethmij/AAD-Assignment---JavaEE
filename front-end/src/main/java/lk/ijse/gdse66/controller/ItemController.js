import {item} from "../model/Item.js";
import {customerDetail, itemDetail} from "../db/DB.js";
import {setItemCode} from "./OrderController.js";

let itemCode = $('#txtItemCode');
let itemName = $('#txtItemName');
let itemQuantity = $('#txtItemQuantity');
let itemPrice = $('#txtItemPrice');
let btnItemSave = $('#btnSave');
let itemCodeList = [];

$(document).on('keydown', function(event) {
    if (event.keyCode === 9) {
        event.preventDefault();
    }
});



btnItemSave.click(function (event){
    if(btnItemSave.text()==="Save ") {

        const userChoice = window.confirm("Do you want to save the item?")
        if (userChoice) {
            getCusIDList( function (IDList) {

                if (!(IDList.includes(itemCode.val()))) {
                    let item = $("#itemForm").serialize();
                    $.ajax({
                        url: "http://localhost:8080/java-pos/item",
                        method: "POST",
                        data: item,
                        success: function (resp) {
                            if (resp.status === 200) {
                                alert(resp.message);
                                $('#cusTBody').append(
                                    `<tr>
                                        <th scope="row">${itemCode.val()}</th>
                                        <td>${itemName.val()}</td>
                                        <td>${itemPrice.val()}</td>
                                        <td>${itemQuantity.val()}</td>
                                        <td style="width: 10%"><img class="delete opacity-75" src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" ></td>
                                     </tr>`
                                );
                                deleteDetail();
                                setFeilds();
                                clearAll(event);
                                setItemCode();
                                btnItemSave.attr("disabled", true);
                            } else if (resp.status === 400) {
                                alert(resp.message);
                            }
                        },
                        error: function (resp) {
                            alert(resp.message);
                        }
                    });
                } else {
                    alert("Duplicate item code!");
                }
            });
        }

    }else if(btnItemSave.text()==="Update ") {
        const userChoice = window.confirm("Do you want to update the item?");
        if (userChoice) {
            let item = {
                "code": itemCode.val(),
                "description": itemName.val(),
                "qtyOnHand": itemQuantity.val(),
                "uPrice": itemPrice.val()
            }

            $.ajax({
                url: "http://localhost:8080/java-pos/item",
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(item),
                success: function (resp) {
                    if (resp.status === 200) {
                        alert(resp.message);
                        getAll();
                        clearAll(event);
                        btnItemSave.text("Save ");
                        btnItemSave.attr("disabled", true);
                        itemCode.attr("disabled", false);
                    } else if (resp.status === 200) {
                        alert(resp.message);
                    }
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
    itemCode.val("");
    itemName.val("");
    itemQuantity.val("");
    itemPrice.val("");
    $('#txtItemCode').css("border","1px solid white");
    $('#itemCodePara').text("");
    $('#txtItemName').css("border","1px solid white");;
    $('#itemNamePara').text("");
    $('#txtItemQuantity').css("border","1px solid white");;
    $('#itemQtyPara').text("");
    $('#txtItemPrice').css("border","1px solid white");;
    $('#itemPricePara').text("");
    btnItemSave.text("Save ");
    btnItemSave.attr("disabled", true);
    event.preventDefault();
    itemCode.attr("disabled", false);
}



$('#getAll').click(function (){
    getAll();

})

function getAll() {
    $.ajax({
        url: "http://localhost:8080/java-pos/customer?option=GET",
        method: "GET",
        success: function (resp) {
            if(resp.status===200){
                $("#cusTBody").empty();
                for (let respElement of resp.data) {
                    $("#cusTBody").append(`<tr>
                        <th scope="row">${respElement.cusID}</th>
                        <td>${respElement.cusName}</td>
                        <td>${respElement.cusAddress}</td>
                        <td>${respElement.cusSalary}</td>
                        <td style="width: 10%"><img  class="delete"  src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                </tr>`);
                    deleteDetail();
                    setFeilds();
                }

            }else if (resp.status === 200){
                alert(resp.message);
            }
        }
    })

}

setFeilds();

function setFeilds() {
    $('#cusTBody>tr').click(function () {
        itemCode.val($(this).children(':eq(0)').text());
        itemName.val($(this).children(':eq(1)').text());
        itemQuantity.val($(this).children(':eq(2)').text());
        itemPrice.val($(this).children(':eq(3)').text());
        btnItemSave.text("Update ");
        btnItemSave.attr("disabled", false);
        itemCode.attr("disabled", true);
    })
}

deleteDetail();

function deleteDetail() {
    let btnDelete = $('.delete');
    btnDelete.on("mouseover", function (){
        $(this).css("cursor", "pointer");}
    )

    btnDelete.click(function () {
        var userChoice = window.confirm("Do you want to delete the item?");

        if (userChoice) {
            $(this).parents('tr').remove();
            let code = $( $(this).parents('tr').children(':nth-child(1)')).text();

            $.ajax({
                url: "http://localhost:8080/java-pos/item?code="+code,
                method: "DELETE",
                success: function (resp){
                    if(resp.status===200){
                        alert(resp.message);
                    }else if(resp.status===400){
                        alert(resp.message);
                    }
                },
                error: function (resp) {

                }
            });
            setItemCode();
        }
    })
}

$('#btnSearch').click(function (){

    let id = $('#txtSearch').val();
    let tbody = $('#cusTBody');
    let count = 0;
    if(id.length!==0) {
        getCusIDList(function (IDList) {
            if (IDList.includes(id)) {
                $.ajax({
                    url: "http://localhost:8080/java-pos/customer?option=SEARCH&cusID=" + id,
                    method: "GET",
                    success: function (resp) {
                        if (resp.status === 200) {
                            tbody.empty();
                            tbody.append(`<tr>
                    <th scope="row">${resp.data[0].cusID}</th>
                        <td>${resp.data[0].cusName}</td>
                        <td>${resp.data[0].cusAddress}</td>
                        <td>${resp.data[0].cusSalary}</td>
                        <td style="width: 10%"><img class="delete" src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                   </tr>`);
                            deleteDetail();
                            setFeilds();
                        } else if (resp.status === 400) {
                            alert(resp.message);
                        }
                    },
                    error: function () {

                    }
                });
            } else {
                alert("No such Customer..please check the ID");
            }
        });
    }else {
        alert("Please enter the ID");
    }
});

function getCusIDList(callback) {
    $.ajax({
        url: "http://localhost:8080/java-pos/customer?option=ID",
        method: "GET",
        success: function (resp) {
            for (let respElement of resp.data) {
                cusIdList.push(respElement);
            }
            callback(cusIdList);
        },
        error:function (resp){
            alert(resp);
        }
    });
}

