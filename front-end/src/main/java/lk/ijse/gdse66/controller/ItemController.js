import {setItemCode} from "./OrderController.js";
import {item} from "../model/Item.js";

let itemCode = $("#txtItemCode");
let itemName = $("#txtItemName");
let itemQuantity = $("#txtItemQuantity");
let itemPrice = $("#txtItemPrice");
let btnItemSave = $('#itemSave');
let itemCodeList = [];

$(document).on('keydown', function(event) {
    if (event.keyCode === 9) {
        event.preventDefault();
    }
});

getAll();

btnItemSave.click(function (event){

    if(btnItemSave.text()==="Save ") {

        const userChoice = window.confirm("Do you want to save the item?")
        if (userChoice) {
            getItemCodeList( function (IDList) {

                if (!(IDList.includes(itemCode.val()))) {
                    let item = $("#itemForm").serialize();
                    $.ajax({
                        url: "http://localhost:8080/java-pos/item",
                        method: "POST",
                        data: item,
                        success: function (resp) {
                            if (resp.status === 200) {
                                alert(resp.message);
                                $('#itemBody').append(
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
            let newItem = Object.assign({}, item);
            newItem.code = itemCode.val();
            newItem.description = itemName.val();
            newItem.uPrice = itemPrice.val();
            newItem.qtyOnHand = itemQuantity.val();

            $.ajax({
                url: "http://localhost:8080/java-pos/item",
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(newItem),
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

$('#itemClear').click(function (event){
    clearAll(event);
})

function clearAll(event) {
    itemCode.val("");
    itemName.val("");
    itemQuantity.val("");
    itemPrice.val("");
    $('#txtItemCode').css("border","1px solid white");
    $('#itemCodePara').text("");
    $('#txtItemName').css("border","1px solid white");
    $('#itemNamePara').text("");
    $('#txtItemQuantity').css("border","1px solid white");
    $('#itemQtyPara').text("");
    $('#txtItemPrice').css("border","1px solid white");
    $('#itemPricePara').text("");
    btnItemSave.text("Save ");
    btnItemSave.attr("disabled", true);
    event.preventDefault();
    itemCode.attr("disabled", false);
}



$('#itemGetAll').click(function (){
    getAll();

})

function getAll() {
    $.ajax({
        url: "http://localhost:8080/java-pos/item?option=GET",
        method: "GET",
        success: function (resp) {
            if(resp.status===200){
                let itemBody = $("#itemBody");
                itemBody.empty();
                for (let respElement of resp.data) {
                    itemBody.append(`<tr>
                        <th scope="row">${respElement.code}</th>
                        <td>${respElement.description}</td>
                        <td>${respElement.qtyOnHand}</td>
                        <td>${respElement.uPrice}</td>
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
    $('#itemBody>tr').click(function () {
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

export function getItemList(code, callback) {
    $.ajax({
        url: "http://localhost:8080/java-pos/item?option=SEARCH&code=" + code,
        method: "GET",
        success: function (resp) {
            callback(resp);
        },
        error: function (resp) {
            alert(resp);
        }
    });
}

$('#itemSearch').click(function (){
    let code = $('#txtItemSearch').val();
    let tbody = $('#itemBody');

    if(code.length!==0) {
        getItemCodeList(function (IDList) {
            if (IDList.includes(code)) {
                getItemList(code, function (resp) {
                    if (resp.status === 200) {
                        tbody.empty();
                        tbody.append(`<tr>
                    <th scope="row">${resp.data[0].code}</th>
                        <td>${resp.data[0].description}</td>
                        <td>${resp.data[0].qtyOnHand}</td>
                        <td>${resp.data[0].uPrice}</td>
                        <td style="width: 10%"><img class="delete" src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                   </tr>`);
                        deleteDetail();
                        setFeilds();
                    } else if (resp.status === 400) {
                        alert(resp.message);
                    }
                });

            } else {
                alert("No such Item..please check the Code");
            }
        });
    }else {
        alert("Please enter the Code");
    }
});

export function getItemCodeList(callback) {
    $.ajax({
        url: "http://localhost:8080/java-pos/item?option=ID",
        method: "GET",
        success: function (resp) {
            for (let respElement of resp.data) {
                itemCodeList.push(respElement);
            }
            callback(itemCodeList);
        },
        error:function (resp){
            alert(resp);
        }
    });
}

