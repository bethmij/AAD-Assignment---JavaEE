import {setItemCode} from "./OrderController.js";
import {item} from "../model/Item.js";
import {setItemCount} from "./DashboardController.js";


let itemCode = $("#txtItemCode");
let itemName = $("#txtItemName");
let itemQuantity = $("#txtItemQuantity");
let itemPrice = $("#txtItemPrice");
let btnItemSave = $('#itemSave');


$(document).on('keydown', function(event) {
    if (event.keyCode === 9) {
        event.preventDefault();
    }
});

getAll();

btnItemSave.click(function (event){
    let newItem = Object.assign({}, item);
    newItem.itemCode = itemCode.val();
    newItem.description = itemName.val();
    newItem.unitPrice = itemPrice.val();
    newItem.qtyOnHand = itemQuantity.val();

    if(btnItemSave.text()==="Save ") {

        swal("Do you want to save the item?", {
            buttons: {
                cancel: "Cancel",
                ok: {
                    text: "OK",
                    value: "catch",
                }
            },
        }).then((value) => {
            if (value === "catch") {

                getItemCodeList(function (IDList) {

                    if (!(IDList.includes(itemCode.val()))) {
                        $.ajax({
                            url: "http://localhost:8000/java-pos/item",
                            method: "POST",
                            contentType: "application/json",
                            data: JSON.stringify(newItem),
                            success: function (resp, status, xhr) {
                                if (xhr.status === 200) {
                                    swal("Saved", resp, "success");
                                    getAll();
                                    deleteDetail();
                                    setFeilds();
                                    clearAll(event);
                                    getItemCodeList(function (CodeList) {
                                        setItemCode(CodeList)
                                    });
                                    btnItemSave.attr("disabled", true);
                                    setItemCount();
                                }
                            },
                            error: function (xhr, status, error) {
                                swal("Error", xhr.responseText, "error");
                            }
                        });
                    } else {
                        swal("Error", "Duplicate item code!", "error");
                    }
                });
            }
        });

    }else if(btnItemSave.text()==="Update ") {

        swal("Do you want to update the item?", {
            buttons: {
                cancel: "Cancel",
                ok: {
                    text: "OK",
                    value: "catch",
                }
            },
        }).then((value) => {
            if (value === "catch") {

                newItem.qtyOnHand = itemQuantity.val();

                $.ajax({
                    url: "http://localhost:8000/java-pos/item",
                    method: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify(newItem),
                    success: function (resp, status, xhr) {
                        if (xhr.status === 200) {
                            swal("Updated", resp, "success");
                            getAll();
                            clearAll(event);
                            btnItemSave.text("Save ");
                            btnItemSave.attr("disabled", true);
                            itemCode.attr("disabled", false);
                        }
                    },
                    error: function (xhr) {
                        swal("Error", xhr.responseText, "error");
                    }
                });
            }
        });
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
        url: "http://localhost:8000/java-pos/item?option=GET",
        method: "GET",
        success: function (resp, status, xhr) {
            if(xhr.status===200) {
                let itemBody = $("#itemBody");
                itemBody.empty();
                for (let item of resp) {
                    itemBody.append(`<tr>
                        <th scope="row">${item.itemCode}</th>
                        <td>${item.description}</td>
                        <td>${item.unitPrice}</td>
                        <td>${item.qtyOnHand}</td>
                        <td style="width: 10%;"><img  class="delete"  src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                </tr>`);
                    deleteDetail();
                    setFeilds();
                }
            }
        },
        error: function (xhr) {
            swal("Error", xhr.responseText, "error");
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

    btnDelete.click(function (event) {

        swal("Do you want to delete the item?", {
            buttons: {
                cancel: "Cancel",
                ok: {
                    text: "OK",
                    value: "catch",
                }
            },
        }).then((value) => {
            if (value === "catch") {

                let deleteRow = $(this).parents('tr');
                let code = $(deleteRow.children(':nth-child(1)')).text();

                $.ajax({
                    url: "http://localhost:8000/java-pos/item?itemCode=" + code,
                    method: "DELETE",
                    success: function (resp, status, xhr) {
                        if (xhr.status === 200) {
                            swal("Deleted", resp, "success");
                            deleteRow.remove();
                            clearAll(event);
                            getItemCodeList(function (CodeList) {
                                setItemCode(CodeList);
                            });
                            setItemCount();
                        }
                    },
                    error: function (xhr) {
                        swal("Error", xhr.responseText, "error");
                    }
                });
                setItemCode();
            }
        });
    })
}

export function getItemList(code, callback) {
    $.ajax({
        url: "http://localhost:8000/java-pos/item?option=SEARCH&itemCode=" + code,
        method: "GET",
        success: function (resp, status, xhr) {
            if (xhr.status === 200) {
                callback(resp,xhr);
            }
        },
        error: function (xhr) {
            swal("Error", xhr.responseText, "error");
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
                        tbody.empty();
                        tbody.append(`
                            <tr>
                                <th scope="row">${resp.itemCode}</th>
                                <td>${resp.description}</td>
                                <td>${resp.unitPrice}</td>
                                <td>${resp.qtyOnHand}</td>
                                <td style="width: 10%;"><img class="delete" src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" style="opacity: 100%;"></td>
                            </tr>`);
                        deleteDetail();
                        setFeilds();
                });

            } else {
                swal("Error", "No such Item..please check the Code", "error");
            }
        });
    }else {
        swal("Error", "Please enter the Code", "error");
    }
});

export function getItemCodeList(callback) {
    let itemCodeList = [];
    $.ajax({
        url: "http://localhost:8000/java-pos/item?option=ID",
        method: "GET",
        success: function (resp, status, xhr) {
            if(xhr.status === 200) {
                itemCodeList = resp;
                callback(itemCodeList);
            }
        },
        error: function (xhr, status, error){
            swal("Error", xhr.responseText, "error");
        }
    });
}

