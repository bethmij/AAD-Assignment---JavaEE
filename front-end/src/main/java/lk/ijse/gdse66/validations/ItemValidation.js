let txtItemCode = $('#txtItemCode');
let paraCode = $('#itemCodePara');
let txtItemName = $('#txtItemName');
let paraItemName = $('#itemNamePara');
let txtPrice = $('#txtItemPrice');
let paraPrice = $('#itemPricePara');
let txtQty = $('#txtItemQuantity');
let paraQty = $('#itemQtyPara');
let btnItemSave = $('#itemSave');
let itemCodeRegEx = /^(I00-)[0-9]{3}$/;
let priceRegEx = /^[0-9]*(\.[0-9]{0,2})?$/;
let qtyRegEx = /^\d+$/;
btnItemSave.attr("disabled", true);


txtItemCode.keyup(function (){


    if( txtItemCode.val().length === 0) {
        txtItemCode.css("border", "1px solid white");
        paraCode.text("");
        btnItemSave.attr("disabled", true);
    }else {
        if (!itemCodeRegEx.test(txtItemCode.val())) {
            txtItemCode.css("border", "1px solid red");
            paraCode.text("Item code is a required field : Pattern I00-000");
            btnItemSave.attr("disabled", true);
        } else {
            txtItemCode.css("border", "1px solid green");
            paraCode.text("");
            console.log(txtItemCode.css("border-left-color"));
            enableItemButton();
        }
    }
})

txtItemName.keyup(function (){

    if( txtItemName.val().length === 0) {
        txtItemName.css("border", "1px solid white");
        paraItemName.text("");
        btnItemSave.attr("disabled", true);
    }else {
        if (!(txtItemName.val().length >= 5) || !(txtItemName.val().length <= 50)) {
            txtItemName.css("border", "1px solid red");
            paraItemName.text("Item Name is a required field : Min 5, Max 20, Spaces Allowed");
            btnItemSave.attr("disabled", true);
        } else {
            txtItemName.css("border", "1px solid green");
            paraItemName.text("");
            enableItemButton();
        }
    }
})

txtPrice.keyup(function (){


    if( txtPrice.val().length === 0) {
        txtPrice.css("border", "1px solid white");
        paraPrice.text("");
        btnItemSave.attr("disabled", true);
    }else {
        if (!priceRegEx.test(txtPrice.val())) {
            txtPrice.css("border", "1px solid red");
            paraPrice.text("Item Price is a required field : Pattern 100.00 or 100");
            btnItemSave.attr("disabled", true);
        } else {
            txtPrice.css("border", "1px solid green");
            paraPrice.text("");
            enableItemButton();
        }
    }
})

txtQty.keyup(function (){


    if( txtQty.val().length === 0) {
        txtQty.css("border", "1px solid white");
        paraQty.text("");
        btnItemSave.attr("disabled", true);
    }else {
        if (!qtyRegEx.test(txtQty.val())) {
            txtQty.css("border", "1px solid red");
            paraQty.text("Item quantity is a required field : only numbers");
            btnItemSave.attr("disabled", true);
        } else {
            txtQty.css("border", "1px solid green");
            paraQty.text("");
            enableItemButton();
        }
    }
})


function enableItemButton() {
    if( itemCodeRegEx.test(txtItemCode.val()) && priceRegEx.test(txtPrice.val()) && qtyRegEx.test(txtQty.val()) &&
        (txtItemName.val().length >= 5) && (txtItemName.val().length <= 50)) {
        btnItemSave.attr("disabled", false);
    }
}

