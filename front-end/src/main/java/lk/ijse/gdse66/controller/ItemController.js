import {setItemCode} from "./OrderController.js";
import {item} from "../model/Item.js";

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
        const userChoice = window.confirm("Do you want to save the item?")
        if (userChoice) {
            getItemCodeList( function (IDList) {

                if (!(IDList.includes(itemCode.val()))) {
                    $.ajax({
                        url: "http://localhost:8000/java-pos/item",
                        method: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(newItem),
                        success: function (resp, status, xhr) {
                            if (xhr.status === 200) {
                                alert(resp)
                                getAll();
                                deleteDetail();
                                setFeilds();
                                clearAll(event);
                                getItemCodeList(function (CodeList) {
                                   setItemCode(CodeList)
                                });
                                btnItemSave.attr("disabled", true);
                            }
                        },
                        error: function (xhr, status, error) {
                            alert("Error : "+xhr.responseText)
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
            newItem.qtyOnHand = itemQuantity.val();

            $.ajax({
                url: "http://localhost:8000/java-pos/item",
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(newItem),
                success: function (resp, status, xhr) {
                    if (xhr.status === 200) {
                        alert(resp)
                        getAll();
                        clearAll(event);
                        btnItemSave.text("Save ");
                        btnItemSave.attr("disabled", true);
                        itemCode.attr("disabled", false);
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
                        <td>${item.qtyOnHand}</td>
                        <td>${item.unitPrice}</td>
                        <td style="width: 10%"><img  class="delete"  src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                </tr>`);
                    deleteDetail();
                    setFeilds();
                }
            }
        },
        error: function (xhr, status, error){
            console.log("Error : ", xhr.statusText);
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
        const userChoice = window.confirm("Do you want to delete the item?");

        if (userChoice) {
            let deleteRow = $(this).parents('tr');
            let code = $( deleteRow.children(':nth-child(1)')).text();

            $.ajax({
                url: "http://localhost:8000/java-pos/item?itemCode="+code,
                method: "DELETE",
                success: function (resp, status, xhr){
                    if(xhr.status === 200) {
                        deleteRow.remove();
                        clearAll(event);
                        getItemCodeList(function (CodeList) {
                            setItemCode(CodeList);
                        });
                        alert(resp);
                    }
                },
                error: function (xhr) {
                    alert("Error : "+xhr.responseText)
                    console.log("Error : ", xhr.statusText);
                }
            });
            setItemCode();
        }
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
                        tbody.empty();
                        tbody.append(`
                            <tr>
                                <th scope="row">${resp.itemCode}</th>
                                <td>${resp.description}</td>
                                <td>${resp.qtyOnHand}</td>
                                <td>${resp.unitPrice}</td>
                                <td style="width: 10%"><img class="delete" src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                            </tr>`);
                        deleteDetail();
                        setFeilds();
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
            console.log("Error : ", xhr.statusText);
        }
    });
}

