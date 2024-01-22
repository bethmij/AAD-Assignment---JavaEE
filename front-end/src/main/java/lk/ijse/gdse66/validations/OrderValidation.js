let txtOrderQty = $('#orderQuantity');
let paraOrderQty = $('#orderQtyPara');
let btnOrderSave = $('#btnAddCart');
let OrderQtyRegEx = /^\d+$/;

btnOrderSave.attr("disabled", true);

txtOrderQty.keyup(function (){
    enableButton();

    if( txtOrderQty.val().length === 0) {
        txtOrderQty.css("border", "1px solid white");
        paraOrderQty.text("");
        btnOrderSave.attr("disabled", true);
    }else {
        if (!OrderQtyRegEx.test(txtOrderQty.val())) {
            txtOrderQty.css("border", "1px solid red");
            paraOrderQty.text("Order quantity is a required field : only numbers");
            btnOrderSave.attr("disabled", true);
        } else {
            txtOrderQty.css("border", "1px solid green");
            paraOrderQty.text("");
        }
    }
})

function enableButton(){
    if( OrderQtyRegEx.test(txtOrderQty.val()) && $('#cusName').val()!=="Customer Name : " && $('#itemName').val()!=="Item Name : "){
        btnOrderSave.attr("disabled", false);
    }
}

$('#cusID').change(function () {enableButton()})
$('#itemCode').change(function () {enableButton()})