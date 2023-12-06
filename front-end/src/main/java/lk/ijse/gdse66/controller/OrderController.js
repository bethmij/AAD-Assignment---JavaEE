import {order} from "../model/Order.js";
import {orderDetails} from "../model/OrderDetail.js";
import {getCusIDList} from "./CustomerController.js";
import {getItemCodeList} from "./ItemController.js";
import {getCustomerList} from "./CustomerController.js";
import {getItemList} from "./ItemController.js";

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
let tbRow, tblQty, tblPrice, currOID;

setCusID();
setOrderID();
setItemCode();

export function setCusID() {
    selectCusOp.empty();
    selectCusOp.append(`<option class="text-white">Customer ID</option>`);

    getCusIDList(function (IDList) {
        for (const id of IDList) {
            selectCusOp.append(`<option class="text-white">${id}</option>`);
        }
    });
}

export function setItemCode() {
    selectItemOp.empty();
    selectItemOp.append(`<option class="text-white">Item Code</option>`);

    getItemCodeList(function (IDList) {
        for (const id of IDList) {
            selectItemOp.append(`<option class="text-white">${id}</option>`);
        }
    });
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
    $.ajax({
        url:"http://localhost:8080/java-pos/customer?option=SEARCH&cusID="+cusID,
        method: "GET",
        success: function (resp) {
            if(resp.status===200){
                if(cusID !== "Customer ID" ) {
                    $('#cusName').val(`Customer Name : ${resp.data[0].cusName}`);
                    $('#cusAddress').val(`Customer Address : ${resp.data[0].cusAddress}`);
                    $('#cusSalary').val(`Customer Salary : ${resp.data[0].cusSalary}`);
                }else {
                    $('#cusName').val(`Customer Name : `);
                    $('#cusAddress').val(`Customer Address : `);
                    $('#cusSalary').val(`Customer Salary : `);
                    btnSave.attr("disabled", true);
                }
            }else if(resp.status===400){
                alert(resp.message);
            }
        },
        error:function (resp) {
            alert(resp);
        }
    });
})



selectItemOp.change(function () {
    let itemCode = selectItemOp.val();
    $.ajax({
        url:"http://localhost:8080/java-pos/item?option=SEARCH&code="+itemCode,
        method: "GET",
        success: function (resp) {
            if(resp.status===200){
                if(itemCode !== "Item Code" ) {
                    txtItemName.val(`Item Name : ${resp.data[0].description}`);
                    txtItemPrice.val(`Item Price : ${resp.data[0].uPrice}`);
                    txtItemQty.val(`Item Quantity : ${resp.data[0].qtyOnHand}`);
                }else {
                    txtItemName.val(`Item Name : `);
                    txtItemPrice.val(`Item Price : `);
                    txtItemQty.val(`Item Quantity : `);
                    txtOrderQty.val("");
                    txtOrderQty.css("border", "1px solid white");
                    btnSave.attr("disabled", true);
                }
            }else if(resp.status===400){
                alert(resp.message);
            }
        },
        error:function (resp) {
            alert(resp);
        }
    });
})



btnSave.click(function (event){
    let itemName = txtItemName.val().split("Item Name : ");
    let itemPrice = txtItemPrice.val().split("Item Price : ");
    if(btnSave.text().includes("Add to Cart")) {
        let itemQty = txtItemQty.val().split("Item Quantity : ");

        if (parseInt(itemQty[1]) >= parseInt(txtOrderQty.val())) {
                $('#orderTbody').append(
                    `<tr>
                        <th scope="row">${selectItemOp.val()}</th>
                        <td>${itemName[1]}</td>
                        <td>${itemPrice[1]}</td>
                        <td>${txtOrderQty.val()}</td>
                        <td style="width: 10%"><img class="orderDelete" src="../resources/assests/img/icons8-delete-96.png" alt="Logo" width="50%" class="opacity-75"></td>
                    </tr>`
                );
                setFeilds();
                deleteDetail();
                calcTotal(itemPrice[1], txtOrderQty.val());
        } else {
            alert("Stock unavailable!");
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
        const userChoice = window.confirm("Do you want to delete the item?");

        if (userChoice) {
            $(this).parents('tr').remove();
            let tablePrice = $(this).parents('tr').children(':nth-child(3)').text();
            let tableQty = $(this).parents('tr').children(':nth-child(4)').text();
            total1 -= (tablePrice*tableQty);
            $('#total-text').text(`Total : ${total1.toFixed(2)}`);
            subTotalTxt.text(`Sub Total : ${total1.toFixed(2)}`);
        }
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
    let orderBody = $("#orderTbody");
    let tableCode = orderBody.children('tr').children(':nth-child(1)');
    let tablePrice = orderBody.children('tr').children(':nth-child(3)');
    let tableQty = orderBody.children('tr').children(':nth-child(4)');
    let newOrderDetailArray = []

    for (let i = 1; i < tableCode.length; i++) {
        let newOrderDetails = Object.assign({}, orderDetails);
        newOrderDetails.oid = orderID;
        newOrderDetails.code = $(tableCode[i]).text();
        newOrderDetails.unitPrice = parseInt($(tablePrice[i]).text());
        newOrderDetails.qty = parseInt($(tableQty[i]).text());
        newOrderDetailArray.push(newOrderDetails);

    }
    console.log(newOrderDetailArray)
    let newOrder = Object.assign({}, order);
    newOrder.oid = oID[1];
    newOrder.date = currDate[1];
    newOrder.customerID = selectCusOp.val();
    newOrder.orderDetails = newOrderDetailArray;
    return newOrder;


}

btnOrder.click(function (event){
    let tableCode = $('#orderTbody').children('tr').children(':nth-child(1)');
    if($(tableCode[1]).text()!==0) {
        var userChoice = window.confirm("Do you want to continue?");

        if (userChoice) {
            let oID = $("#orderID").val().split("Order ID : ");
            let orderID = oID[1];
            let currDate = $('#currDate').text().split("Date : ");

            if (btnOrder.text().includes("Place Order")) {
                if (cash.val() !== "") {
                    if (!(parseInt(cash.val()) < total1)) {
                        let order = setOrderArray(orderID, oID, currDate);
                        $.ajax({
                           url: "http://localhost:8080/java-pos/order",
                            method: "POST",
                            data: JSON.stringify(order),
                            success: function (resp) {
                                if (resp.status === 200) {
                                    alert(resp.message);
                                    clearItemSelect();
                                    clearCusDetail();
                                    clearTotal();
                                    setOrderID();
                                }else  if (resp.status === 400){
                                    alert(resp.message);
                                }
                            }
                        });
                    } else {
                        alert("Insufficient payment amount")
                    }
                } else {
                    alert("Please add ur payment")
                }
            } else if (btnOrder.text().includes("Update Order")) {
                let order = setOrderArray(orderID, oID, currDate);
                $.ajax({
                    url:"http://localhost:8080/java-pos/order",
                    method: "PUT",
                    data: JSON.stringify(order),
                    success:function (resp){
                        if (resp.status === 200) {
                            alert(resp.message);
                            clearItemSelect();
                            clearCusDetail();
                            clearTotal();
                            $('#orderID').val(`Order ID : ${currOID[1]}`);
                            btnOrder.text("");
                            btnOrder.append(`<img src="../resources/assests/img/Screenshot__550_-removebg-preview.png" alt="Logo" width="25vw" class="opacity-50 me-2">Place Order`);
                            cash.attr("disabled", false);
                            discount.attr("disabled", false);
                            $('#txtOrderSearch').val("");
                            $("#orderSearch").removeClass('btn-outline-danger').addClass('btn-outline-success');
                            $('#orderSearch').text("Search");
                        }else if(resp.status===400){
                            alert(resp.message);
                        }
                    },
                    error:function (resp){
                        alert(resp);
                    }
                });
            }

        }
    }else {
        alert("Add items to your cart");
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

        getItemList(itemCode,function (resp) {
            if(resp.status===200){
                txtItemQty.val(`Item Quantity : ${resp.data[0].qtyOnHand}`);
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
    $("#orderTbody").empty();
    $('#orderTbody').append(`<tr >
        <th scope="col">Code</th>
        <th scope="col">Name</th>
        <th scope="col">Price</th>
        <th scope="col">Order Qty</th>
        <th scope="col"></th>
    </tr>`);
    total1 = parseInt(total[0]);
}

$('#orderSearch').click(function (){
    let id = $('#txtOrderSearch').val();
    let tbody = $('#orderTbody');

    if(id.length!==0) {
        getOrderIDList(function (IDList) {
            if (IDList.includes(id)) {
                getOrderList(id, function (order) {
                    $('#orderID').val("Order ID : " + order.oid);
                    getCustomerList(order.customerID, function (resp) {
                        if (resp.status === 200) {
                            selectCusOp.val(resp.data[0].cusID);
                            $('#cusName').val(`Customer Name : ${resp.data[0].cusName}`);
                            $('#cusAddress').val(`Customer Address : ${resp.data[0].cusAddress}`);
                            $('#cusSalary').val(`Customer Salary : ${resp.data[0].cusSalary}`);
                            tbody.empty();
                            tbody.append(`<tr >
                                    <th scope="col">Code</th>
                                    <th scope="col">Name</th>
                                    <th scope="col">Price</th>
                                    <th scope="col">Order Qty</th>
                                    <th scope="col"></th>
                             </tr> `);
                            for (const item of order.orderDetails) {
                                getItemList(item.code, function (resp) {
                                    if (resp.status === 200) {
                                        tbody.append(
                                            `<tr>
                                        <th scope="row">${resp.data[0].code}</th>
                                        <td>${resp.data[0].description}</td>
                                        <td>${resp.data[0].uPrice}</td>
                                        <td>${item.qty}</td>                        
                                        <td style="width: 10%"><img class="orderDelete" src="../resources/assests/img/icons8-delete-96.png"
                                                                        alt="Logo" width="50%" class="opacity-75"></td>
                                    </tr>`)
                                        setFeilds();
                                        deleteDetail();
                                        calcTotal(resp.data[0].uPrice, item.qty);
                                        btnOrder.text("");
                                        btnOrder.append(`<img src="../resources/assests/img/Screenshot__550_-removebg-preview.png" 
                                                             alt="Logo" width="25vw" class="opacity-50 me-2">Update Order`);
                                    } else if (resp.status === 400) {
                                        alert(resp.message);
                                    }
                                });
                            }

                        } else if (resp.status === 400) {
                            alert(resp.message);
                        }
                    })
                });
            } else {
                alert("No such Order..please check the order ID");
            }
        });
    }else {
        alert("Please enter the order ID");
    }
})

function getOrderIDList(callback) {
    let orderIDList = []
    $.ajax({
        url: "http://localhost:8080/java-pos/order?option=ID",
        method: "GET",
        success: function (resp) {
            if (resp.status === 200) {
                if(resp.data.length !== 0) {
                    for (const respElement of resp.data) {
                        orderIDList.push(respElement);
                    }

                    callback(orderIDList);
                }else {
                    callback(orderIDList);
                }
            }else if(resp.status === 400){
                alert(resp.message);
            }
        },
        error: function (resp) {
            alert(resp);
        }
    })
}

function getOrderList(id,callback) {
    let orderDetail = [];
    $.ajax({
        url: "http://localhost:8080/java-pos/order?option=SEARCH&id="+id,
        method: "GET",
        success: function (resp) {
            if (resp.status === 200) {

                for (const item of resp.data[0].items) {
                    orderDetail.push(item);
                }
                let newOrder = Object.assign({}, order);
                newOrder.oid = resp.data[0].oid;
                newOrder.date = resp.data[0].date;
                newOrder.customerID = resp.data[0].customerID;
                newOrder.orderDetails = orderDetail;
                callback(newOrder);

            }else if(resp.status === 400){
                alert(resp.message);
            }
        },
        error: function (resp) {
            alert(resp);
        }
    })
}


