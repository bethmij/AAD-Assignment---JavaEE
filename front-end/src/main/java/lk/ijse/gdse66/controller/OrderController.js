import {order} from "../model/Order.js";
import {orderDetails} from "../model/OrderDetail.js";
import {getCusIDList} from "./CustomerController.js";
import {getItemCodeList} from "./ItemController.js";
import {getCustomerList} from "./CustomerController.js";
import {getItemList} from "./ItemController.js";
import {seOrderCount} from "./DashboardController.js";

let selectCusOp = $('#cusID');
let selectItemOp = $('#itemCode');
let btnSave = $('#btnAddCart');
let txtItemName = $('#itemName');
let txtItemPrice = $('#itemPrice');
let txtItemQty = $('#itemQuantity');
let txtOrderQty = $('#orderQuantity');
let totalTxt = $("#total-text").text().split("Total : ");
let subTotalTxt = $("#subTotal-text");
let total = totalTxt[1].split(".");
let total1 = parseInt(total[0]);
let cash = $("#cash");
let discount = $("#discount");
let btnOrder = $('#btnPlaceOrder');
let orderBody = $("#orderTbody");
let btnSearch = $('#orderSearch');
let tbRow, tblQty, tblPrice;
setCusID();
setOrderID();
setItemCode();

export function setCusID(IDList) {
    selectCusOp.empty();
    selectCusOp.append(`<option class="text-white">Customer ID</option>`);

    if (IDList === undefined) {
        getCusIDList(function (IDList) {
            for (const id of IDList) {
                selectCusOp.append(`<option class="text-white">${id}</option>`);
            }
        });
    }else {
        for (const id of IDList) {
            selectCusOp.append(`<option class="text-white">${id}</option>`);
        }
    }
}

export function setItemCode(CodeList) {
    selectItemOp.empty();
    selectItemOp.append(`<option class="text-white">Item Code</option>`);

    if (CodeList === undefined) {
        getItemCodeList(function (CodeList) {
            for (const code of CodeList) {
                selectItemOp.append(`<option class="text-white">${code}</option>`);
            }
        });
    }else {
        for (const code of CodeList) {
            selectItemOp.append(`<option class="text-white">${code}</option>`);
        }
    }
}

function setOrderID() {
    let orderID = $('#orderID');
    getOrderIDList(function (orderList) {
        orderList.sort((a, b) => {
            const [, numA] = a.match(/(\d+)$/);
            const [, numB] = b.match(/(\d+)$/);
            return parseInt(numB) - parseInt(numA);
        });

        if(orderList.length===0){
            orderID.val(`Order ID : OR00-1`);
        }else {
            let id = orderList[0];
            let num = id.split("OR00-");
            orderID.val(`Order ID : OR00-${parseInt(num[1])+1}`);
        }
    });
}



selectCusOp.change(function () {
    let cusID = selectCusOp.val();

    if(cusID !== "Customer ID") {
        $.ajax({
            url: "http://localhost:8000/java-pos/customer?option=SEARCH&cusID=" + cusID,
            method: "GET",
            success: function (resp, status, xhr) {
                if (xhr.status === 200) {
                    $('#cusName').val(`Customer Name : ${resp.cusName}`);
                    $('#cusAddress').val(`Customer Address : ${resp.cusAddress}`);
                    $('#cusSalary').val(`Customer Salary : ${resp.cusSalary}`);
                }
            },
            error: function (xhr) {
                swal("Error", xhr.responseText, "error");
            }
        });
    }else {
        $('#cusName').val(`Customer Name : `);
        $('#cusAddress').val(`Customer Address : `);
        $('#cusSalary').val(`Customer Salary : `);
        btnSave.attr("disabled", true);
    }
})



selectItemOp.change(function () {
    let itemCode = selectItemOp.val();

    if(itemCode !== "Item Code" ) {
        $.ajax({
            url:"http://localhost:8000/java-pos/item?option=SEARCH&itemCode="+itemCode,
            method: "GET",
            success: function (resp, status, xhr) {
                if(xhr.status===200){
                    txtItemName.val(`Item Name : ${resp.description}`);
                    txtItemPrice.val(`Item Price : ${resp.unitPrice}`);
                    txtItemQty.val(`Item Quantity : ${resp.qtyOnHand}`);
                }
            },
            error: function (xhr) {
                swal("Error", xhr.responseText, "error");
            }
        });
    } else {
        txtItemName.val(`Item Name : `);
        txtItemPrice.val(`Item Price : `);
        txtItemQty.val(`Item Quantity : `);
        txtOrderQty.val("");
        txtOrderQty.css("border", "1px solid white");
        btnSave.attr("disabled", true);
    }
});



btnSave.click(function (event){
    let itemName = txtItemName.val().split("Item Name : ");
    let itemPrice = txtItemPrice.val().split("Item Price : ");
    if(btnSave.text().includes("Add to Cart")) {
        let itemQty = txtItemQty.val().split("Item Quantity : ");

        if (parseInt(itemQty[1]) >= parseInt(txtOrderQty.val())) {
            let isAdded = checkDuplicateItem(selectItemOp.val(), txtOrderQty.val());
            if(!isAdded) {
                $('#orderTbody').append(
                    `<tr>
                        <th scope="row">${selectItemOp.val()}</th>
                        <td>${itemName[1]}</td>
                        <td>${itemPrice[1]}</td>
                        <td>${txtOrderQty.val()}</td>
                        <td style="width: 10%;"><img class="orderDelete" src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                    </tr>`
                );

                setFeilds();
                deleteDetail();
                calcTotal(itemPrice[1], txtOrderQty.val());
            }else {
                calcTotal(itemPrice[1], txtOrderQty.val());
            }
        } else {
            swal("Error", "Stock unavailable!", "error");
        }
    }else if(btnSave.text()==="Update "){
        tbRow.children(':eq(1)').text(itemName[1]);
        tbRow.children(':eq(2)').text(itemPrice[1]);
        tbRow.children(':eq(3)').text(txtOrderQty.val());

        total1 -= (tblPrice*tblQty);
        calcTotal(itemPrice[1], txtOrderQty.val());
        selectItemOp.attr("disabled", false);
        setFeilds();
        deleteDetail();
        clearItemSelect();
    }
    event.preventDefault();
})

deleteDetail();

function deleteDetail() {
    let btnDelete = $('.orderDelete');
    btnDelete.on("mouseover", function (){
        $(this).css("cursor", "pointer")}
    )

    btnDelete.click(function () {

        swal("Do you want to delete the item?", {
            buttons: {
                cancel1: {
                    text: "Cancel",
                    className: "custom-cancel-btn",
                },
                ok: {
                    text: "OK",
                    value: "catch",
                    className: "custom-ok-btn",
                }
            },
        }).then((value) => {
            if (value === "catch") {

                $(this).parents('tr').remove();
                let tablePrice = $(this).parents('tr').children(':nth-child(3)').text();
                let tableQty = $(this).parents('tr').children(':nth-child(4)').text();
                total1 -= (tablePrice * tableQty);
                $('#total-text').text(`Total : ${total1.toFixed(2)}`);
                subTotalTxt.text(`Sub Total : ${total1.toFixed(2)}`);
            }
        });
    })
}

function calcTotal(price, qty) {
    let tot = price*qty;
    total1 += tot;
    $('#total-text').text(`Total : ${total1.toFixed(2)}`);
    subTotalTxt.text(`Sub Total : ${total1.toFixed(2)}`);
}

cash.change(function (){
    let balance = (parseInt( cash.val()) - total1).toFixed(2);
    $('#balance').val(`Balance : ${balance}`);
})

cash.keyup(function (){
    let balance = (parseInt( cash.val()) - total1).toFixed(2);
    $("#balance").val(`Balance : ${balance}`);
    if(cash.val()===""){
        $('#balance').val(`Balance : 0.00`);
    }
})

discount.change(function (){
    let dis = total1 - ((total1*parseInt(discount.val()))/100).toFixed(2);
    subTotalTxt.text(`Sub Total : ${dis}`);
})

discount.keyup(function (){
    let dis = total1 - ((total1*parseInt(discount.val()))/100).toFixed(2);
    subTotalTxt.text(`Sub Total : ${dis}`);
    if(discount.val()===""){
        subTotalTxt.text(`Sub Total : 0.00`);
    }
})

function setOrderArray(orderID, oID, currDate) {

    let tableCode = orderBody.children('tr').children(':nth-child(1)');
    let tablePrice = orderBody.children('tr').children(':nth-child(3)');
    let tableQty = orderBody.children('tr').children(':nth-child(4)');
    let newOrderDetailArray = []

    for (let i = 1; i < tableCode.length; i++) {
        let newOrderDetails = Object.assign({}, orderDetails);
        newOrderDetails.orderId = orderID;
        newOrderDetails.itemCode = $(tableCode[i]).text();
        newOrderDetails.unitPrice = parseInt($(tablePrice[i]).text());
        newOrderDetails.qtyOnHand = parseInt($(tableQty[i]).text());
        newOrderDetailArray.push(newOrderDetails);

    }
    console.log(newOrderDetailArray)
    let newOrder = Object.assign({}, order);
    newOrder.orderId = oID[1];
    newOrder.orderDate = currDate[1];
    newOrder.customerId = selectCusOp.val();
    newOrder.orderDetails = newOrderDetailArray;
    return newOrder;


}

btnOrder.click(function (event){
    let tableCode = $('#orderTbody').children('tr').children(':nth-child(1)');
    if($(tableCode[1]).text()!==0) {
        swal("Do you want to continue?", {
            buttons: {
                cancel1: {
                    text: "Cancel",
                    className: "custom-cancel-btn",
                },
                ok: {
                    text: "OK",
                    value: "catch",
                    className: "custom-ok-btn",
                }
            },
        }).then((value) => {
            if (value === "catch") {

                let oID = $("#orderID").val().split("Order ID : ");
                let orderID = oID[1];
                let currDate = $('#currDate').text().split("Date : ");

                if (btnOrder.text().includes("Place Order")) {
                    if (cash.val() !== "") {
                        if (!(parseInt(cash.val()) < total1)) {
                            let order = setOrderArray(orderID, oID, currDate);
                            console.log(order)
                            $.ajax({
                                url: "http://localhost:8000/java-pos/order",
                                method: "POST",
                                data: JSON.stringify(order),
                                success: function (resp, status, xhr) {
                                    if (xhr.status === 200) {
                                        swal("Order Placed!", resp, "success");
                                        clearItemSelect();
                                        clearCusDetail();
                                        clearTotal();
                                        setOrderID();
                                        seOrderCount();
                                    }
                                },
                                error: function (xhr) {
                                    swal("Error", xhr.responseText, "error");
                                }
                            });
                        } else {
                            swal("Error", "Insufficient payment amount!", "error");
                        }
                    } else {
                        swal("Error", "Please add ur payment", "error");
                    }
                }
            } else if (btnOrder.text().includes("Update Order")) {
                let order = setOrderArray(orderID, oID, currDate);
                $.ajax({
                    url: "http://localhost:8000/java-pos/order",
                    method: "PUT",
                    data: JSON.stringify(order),
                    success: function (resp, status, xhr) {
                        if (xhr.status === 200) {
                            swal("Updated!", resp, "success");
                            updateSearch();
                        }
                    },
                    error: function (xhr) {
                        swal("Error", xhr.responseText, "error");
                    }
                });
            }
        });
    }else {
        swal("Error", "Add items to your cart", "error");
    }
    event.preventDefault();
})

setFeilds();

function setFeilds() {
    $('#orderTbody>tr').click(function () {
        tbRow = $(this);
        tblQty = $(this).children(':eq(3)').text();
        tblPrice = $(this).children(':eq(2)').text();

        let itemCode = $(this).children(':eq(0)').text();
        txtItemName.val(`Item Name : ${$(this).children(':eq(1)').text()}`);
        txtItemPrice.val(`Item Price : ${tblPrice}`);
        txtOrderQty.val(tblQty);
        selectItemOp.val(itemCode);
        selectItemOp.attr("disabled", true);

        getItemList(itemCode,function (resp,xhr) {
            if(xhr.status===200){
                txtItemQty.val(`Item Quantity : ${resp.qtyOnHand}`);
            }
        });
        setFeilds();
        btnSave.text("Update ");
        btnSave.attr("disabled", false);
    })
}

$('#orderClear').click(function (event){
    clearItemSelect();
    event.preventDefault();
})

function clearItemSelect(){
    selectItemOp.val("Item Code");
    txtItemName.val(`Item Name : `);
    txtItemPrice.val(`Item Price : `);
    txtItemQty.val(`Item Quantity : `);
    txtOrderQty.val("");
    txtOrderQty.css("border", "1px solid white");
    btnSave.text("");
    btnSave.append(`<img src="../resources/assests/img/Screenshot__543_-removebg-preview.png" alt="Logo" width="25vw" class="opacity-50 me-3">Add to Cart`);
    btnSave.attr("disabled", true);
}

function clearCusDetail(){
    selectCusOp.val("Customer ID");
    $('#cusName').val(`Customer Name : `);
    $('#cusAddress').val(`Customer Address : `);
    $('#cusSalary').val(`Customer Salary : `);
}

function clearTotal(){
    $('#total-text').text("Total : 00.00");
    $('#subTotal-text').text("Sub Total : 00.00");
    $('#cash').val("");
    $('#discount').val("");
    $('#balance').val("");
    orderBody.empty();
    $('#orderTbody').append(`<tr >
        <th scope="col">Code</th>
        <th scope="col">Name</th>
        <th scope="col">Price</th>
        <th scope="col">Order Qty</th>
        <th scope="col"></th>
    </tr>`);
    total1 = parseInt(total[0]);
}

function updateSearch() {
    clearItemSelect();
    clearCusDetail();
    clearTotal();
    setOrderID();
    btnOrder.text("");
    btnOrder.append(`<img src="../resources/assests/img/Screenshot__550_-removebg-preview.png" alt="Logo"
                                 width="25vw" style="opacity: 50%;">  Place Order`);
    cash.attr("disabled", false);
    discount.attr("disabled", false);
    $('#txtOrderSearch').val("");
    $('#cash').attr("disabled", false);
    $('#discount').attr("disabled", false);
    btnSearch.text("Search");
    btnSearch.toggleClass('btn-outline-danger btn-outline-success');
}

btnSearch.click(function (){
    let id = $('#txtOrderSearch').val();
    let tbody = $('#orderTbody');

    if(btnSearch.text().includes("Search")) {

        if (id.length !== 0) {
            getOrderIDList(function (IDList) {
                if (IDList.includes(id)) {
                    getOrderList(id, function (order) {
                        $('#orderID').val("Order ID : " + order.orderId);
                        getCustomerList(order.customerId, function (resp, xhr) {
                            if (xhr.status === 200) {
                                selectCusOp.val(resp.cusID);
                                $('#cusName').val(`Customer Name : ${resp.cusName}`);
                                $('#cusAddress').val(`Customer Address : ${resp.cusAddress}`);
                                $('#cusSalary').val(`Customer Salary : ${resp.cusSalary}`);
                                tbody.empty();
                                tbody.append(`<tr >
                                    <th scope="col">Code</th>
                                    <th scope="col">Name</th>
                                    <th scope="col">Price</th>
                                    <th scope="col">Order Qty</th>
                                    <th scope="col"></th>
                             </tr> `);
                                for (const item of order.orderDetails) {
                                    getItemList(item.itemCode, function (resp, xhr) {
                                        if (xhr.status === 200) {
                                            tbody.append(
                                                `<tr>
                                        <th scope="row">${resp.itemCode}</th>
                                        <td>${resp.description}</td>
                                        <td>${resp.unitPrice}</td>
                                        <td>${item.qtyOnHand}</td>                        
                                        <td style="width: 10%"><img class="orderDelete" src="../resources/assests/img/icons8-delete-96.png"
                                                                        alt="Logo" width="50%" class="opacity-75"></td>
                                    </tr>`)
                                            setFeilds();
                                            deleteDetail();
                                            calcTotal(resp.unitPrice, item.qtyOnHand);
                                        }
                                    });
                                }
                                $('#cash').attr("disabled", true);
                                $('#discount').attr("disabled", true);
                                btnOrder.text("");
                                btnOrder.append(`<img src="../resources/assests/img/Screenshot__550_-removebg-preview.png" 
                                                             alt="Logo" width="25vw" class="opacity-50 me-2">Update Order`);
                                btnSearch.text("Delete");
                                btnSearch.toggleClass('btn-outline-success btn-outline-danger');
                            }
                        })
                    });
                } else {
                    swal("Error", "No such Order..please check the order ID", "error");
                }
            });
        } else {
            swal("Error", "Please enter the order ID!", "error");
        }
    }else if(btnSearch.text().includes("Delete")) {
        const userChoice = window.confirm("Do you want to delete the order?");
        if (userChoice) {
            $.ajax({
                url: "http://localhost:8000/java-pos/order?ID=" + id,
                method: "DELETE",
                success: function (resp, status, xhr) {
                    if (xhr.status === 200) {
                        swal("Deleted", resp, "success");
                        updateSearch();
                        seOrderCount();
                    }
                },
                error: function (xhr) {
                    swal("Error", xhr.responseText, "error");
                }
            });
        }
    }
})

function getOrderIDList(callback) {
    let orderIDList = []
    $.ajax({
        url: "http://localhost:8000/java-pos/order?option=ID",
        method: "GET",
        success: function (resp, status, xhr) {
            if (xhr.status === 200) {
                if(resp !== 0) {
                   orderIDList = resp;
                   callback(orderIDList);
                }else {
                    callback(orderIDList);
                }
            }
        },
        error: function (xhr) {
            swal("Error", xhr.message, "error");
        }
    })
}

function getOrderList(id,callback) {

    $.ajax({
        url: "http://localhost:8000/java-pos/order?option=SEARCH&id="+id,
        method: "GET",
        success: function (resp, status, xhr) {
            if (xhr.status === 200) {
                let newOrder = Object.assign({}, order);
                newOrder.orderId = resp.orderId;
                newOrder.orderDate = resp.orderDate;
                newOrder.customerId = resp.customerId;
                newOrder.orderDetails = resp.orderDetails;
                callback(newOrder);
            }
        },
        error: function (xhr) {
            swal("Error", xhr.responseText, "error");
        }

    })
}

function checkDuplicateItem(itemCode, itemQty) {
    let tableCode = orderBody.children('tr').children(':nth-child(1)');
    let tableQty = orderBody.children('tr').children(':nth-child(4)');

    for (let i = 1; i < tableCode.length; i++) {
        if(itemCode===$(tableCode[i]).text()){
            let qty = parseInt($(tableQty[i]).text()) + parseInt(itemQty);
            $(tableQty[i]).text(qty)
            return true;
        }
    }
    return false;
}

