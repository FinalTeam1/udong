$(function(){
    url = new URL(location.href);
    urlSearch = url.searchParams;

    if(url.pathname.includes("/share/rent")){
            $('#giveBtn').addClass("gray");
            $('#rentBtn').removeClass("gray");
            $('.rentCom').css('visibility', 'visible');
        } else if(window.location.pathname.includes("/share/give")){
            $('#rentBtn').addClass("gray");
            $('#giveBtn').removeClass("gray");
            $('.rentCom').css('visibility', 'hidden');
        }

    const bodyId = $("body").attr("id");
    if(bodyId == "giveMain" || bodyId == "rentMain"){
        getCatList();

        window.addEventListener("popstate", function(event) {
            if (event.state) {
                restoreFormState(event.state);
                updateItems(event.state);
            }
        });
    }

    if(bodyId == "itemDetail" || bodyId == "registerForm"){
        $('#datePicker').datepicker({
            format: 'yyyy-mm-dd',
            todayHighlight: true,
            startDate: '+1d',
            autoclose : true,
            endDate: '+1m'
        })

        $("input[name='itemGroup']").change(function(){
            let checked = $("input[name='itemGroup']:checked").val();
            if(checked == 'rent'){
                $("#expiryDate").css("visibility", "hidden");
                dateRefresh();
            } else {
                $("#expiryDate").css("visibility", "visible");
                dateRefresh();
            }
        });

    }



});



function getCatList() {
    $.ajax({
            url: "/share/getCatList",
            type: "get",
            success: function(catList) {
                console.log(catList);
                renderCatList(catList);

                const savedState = history.state;
                if (savedState) {
                    restoreFormState(savedState);
                    updateItemList(savedState);
                    console.log(savedState);
                } else {
                    search(1);
                }
            },
            error: function() {
                alert("카테고리 불러오기 실패");
            }
    });
}

function renderCatList(catList) {
    let catResult = catList.map( (cat) => {
        return `
            <option value=${cat.catCode}>${cat.catName}</option>
        `}).join("");

    $('.catSelect').append(catResult);

}

function dateRefresh() {
    $('#datePicker').datepicker("setDate", null);
    $('#datePicker').datepicker({
    format: 'yyyy-mm-dd',
    todayHighlight: true,
    startDate: '+1d',
    autoclose : true,
    endDate: '+1m'
    });
}

let imgCounter = 0;

function addImg() {
    let len = $(".img-group").length;
    if(len < 3){
    imgCounter++;
    let imgId = `img-${imgCounter}`;
    let addform = `
        <div class='img-group'>
            <label for='img-input-${imgCounter}'>
                <div class="btn btn-udh-silver">파일선택</div>
            </label>
            <input id='img-input-${imgCounter}' class='form-control' type='file' name='imgs'  data-img-id='${imgId}' style="display:none">
            <p id="originalName-${imgCounter}" style="display : inline-block" data-img-id='${imgId}'></p>
            <a href='#this' name='img-delete' data-img-id='${imgId}'>삭제</a>
        <div>`
    $("#item-imgList").append(addform);

    $("a[name='img-delete']").off("click").on("click", function(e){
        e.preventDefault();
        deleteImg($(this));
    })

    $("input[name='imgs']").off("change").on("change", function(e){
        e.preventDefault();
        setImgPreview($(this));
    })

    }
}

function deleteImg(img){
    let imgId = img.data('img-id');
    let hiddenInput = $(`input[data-img-id='${imgId}']`);
    if (hiddenInput.length && hiddenInput.attr('name') === 'exImgs') {
        let delFilesNo = $("#delFilesNo").val();
        $("#delFilesNo").val(delFilesNo + hiddenInput.val() + ",");
        hiddenInput.val('');
    }
    if (hiddenInput.length && hiddenInput.attr('name') === 'exImgsName') {
        let delFilesName = $("#delFilesName").val();
        $("#delFilesName").val(delFilesName + hiddenInput.val() + ",");
        hiddenInput.val('');
    }

    $(`#${imgId}`).parent().remove();
    img.parent().remove();
}

function setImgPreview(input){
    if(input[0].files && input[0].files[0]){
        let reader = new FileReader();
        reader.onload = function(e){
            let imgId = input.data('img-id');
            let img = document.createElement("img");
            img.setAttribute("src", e.target.result);
            img.setAttribute("style", "height:60px");
            img.setAttribute("id", imgId);

            let imgDiv = document.createElement("div");
            imgDiv.setAttribute("id", `${imgId}-div`);
            imgDiv.appendChild(img);

            $('#imgPreview').append(imgDiv);
        };
        reader.readAsDataURL(input[0].files[0]);
        let imgId = input.data('img-id');
        $(`p[data-img-id="${imgId}"]`).html(input[0].files[0].name);
        input.prop('disabled', true);
    }

}


function isValid() {
    let isGive = $('input[name=itemGroup]:checked').val();
    let selectedDate = $('#datePicker').val();
    console.log(selectedDate);
    if(isGive == 'give'){
        if(selectedDate == null || selectedDate == ''){
            alert("마감일을 선택해주세요.");
            return false;
        } else{
            return true;
        }
    }
}

function enableAllFileInputs() {
    $("input[name='imgs']").prop('disabled', false);
}

function getSearchParams(page){
    selectedCat = $('.catSelect').val();
    let group = $("input[name='group']").val();
    let isChecked = $('#availableCheck').is(':checked');
    let statusCode = isChecked ? $('#availableCheck').val() : '';
    let keyword = $('#keyword').val();
    if (page == null || page == '') {
        page = 1;
    }
    console.log(selectedCat);
    return {
        catCode: selectedCat,
        group: group,
        statusCode: statusCode,
        keyword: keyword,
        page: page
    };
}

function updateItemList(params){
    $.ajax({
            url: "/share/search",
            type: "get",
            data: params,
            success: function(data) {
                console.log(data)
                renderItemList(data.itemList);
                renderPageNation(data.pageInfo);


                // url에 검색한 쿼리들 넣어주기.
                const newUrl = createUrlWithParams(params);
                history.pushState(params, '', newUrl);
            },
            error: function(data) {
                console.log(data);
                if(data.responseJSON.msg != null){
                    alert(data.responseJSON.msg)
                } else {
                    alert("물건 불러오기 실패");
                    }
            }
    });
}

function renderItemList(items){
    let itemResult = "";
    if(items.length == 0){
        itemResult = `<div class="alert alert-secondary" role="alert" style="width:100%; text-align:center">
                        등록된 물건이 없습니다.
                      </div>`
    } else {
        itemResult = items.map((item) => {

            let head = `
                <div class="col">
                   <div class="card" onclick="location.href='/share/${item.itemGroup}/detail?itemNo=${item.itemNo}'">
            `

            let mid = `
                <div>
                    <img src="${item.img == null ? '/img/noimg.jpg' : '/shaUploadFiles/' + item.img}" class="card-img-top" alt="물건이미지" style="height:20em">

            `
            let tail = `
                    </div>
                        <div class="card-body">
                            <h4>${item.title}</h4>
                            <p>💛 <span>${item.likeCnt}</span>👀 <span>${item.viewCnt}</span>🙋‍♀️ <span>${item.reqCnt}</span></p>
                        </div>
                </div>
            </div>`

            if(item.statusCode != 'AVL'  && item.statusCode != 'GIV'){
                mid = `
                    <div class="div-blur">
                        <img src="${item.img == null ? '/img/noimg.jpg' : '/shaUploadFiles/' + item.img}" class="card-img-top img-blur" alt="물건이미지" style="height:20em">
                `

                if(item.statusCode == 'UNAV'){
                    mid += `
                        <div class="blur-info"><h3>대여불가</h3></div>
                    `
                } else if(item.statusCode == 'RNT'){
                    mid += `
                        <div class="blur-info">
                            <h3>대여중</h3>
                            <p>반납예정일: ${item.returnDate}</p>
                        </div>
                    `
                } else if(item.statusCode == 'GVD'){
                    mid += `
                        <div class="blur-info">
                            <h3>나눔완료</h3>
                            <p>당첨자: </p>
                        </div>
                    `
                }
            }
            return head + mid + tail
        }).join("");
    }
    $("#itemList").html(itemResult);


}

function renderPageNation(pageInfo){
    let totalCounts = pageInfo.totalCounts;
    let startPage = pageInfo.startPage;
    let endPage = pageInfo.endPage;
    let totalPage = pageInfo.totalPage;
    let currentPage = pageInfo.currentPage;

    let pageResult = "";

    if(totalCounts > 0){
        pageResult += `
            <li class="page-item me-1">
                <button class="page-link btn-udh-blue" onclick="search(${currentPage-1})" style="${currentPage == 1 ? 'visibility:hidden' : ''}" aria-label="Previous">
                    <span>&laquo;</span>
                </button>
            </li>`


        for(i = startPage; i <= endPage; i++){
            pageResult += `
                <li class="page-item active me-1" aria-current="page">
                    <button class="page-link btn-udh-blue ${currentPage == i ? 'pageActive' : ''}" onclick="search(${i})">${i}</button>
                </li>`
        }



        pageResult += `
            <li class="page-item me-1">
               <button class="page-link btn-udh-blue " style="${currentPage == totalPage ? 'visibility:hidden' : ''}" onclick="search(${currentPage+1})" aria-label="Next">
                   <span>&raquo;</span>
               </button>
            </li>`
    }

    $('.pagination').html(pageResult);
}


function search(page){
    let params = getSearchParams(page);
    console.log(params);
    updateItemList(params);

}

function createUrlWithParams(params) {
    const url = new URL(window.location.href);
    url.searchParams.set('catCode', params.catCode);
    url.searchParams.set('group', params.group);
    url.searchParams.set('statusCode', params.statusCode);
    url.searchParams.set('keyword', params.keyword);
    url.searchParams.set('page', params.page);
    return url.toString();
}

function restoreFormState(params) {
    $('.catSelect').val(params.catCode).prop("selected", true);
    $("input[name='group']").val(params.group);
    $('#availableCheck').prop('checked', params.statusCode ? true : false);
    $('#keyword').val(params.keyword);
}

function renderItemDetail(item){
    console.log(item);
    $('#likeCnt').text(item.likeCnt);
    $('#viewCnt').text(item.viewCnt);
    $('#reqCnt').text(item.reqCnt);
    console.log(item.liked);
    let img = item.liked == true ? "/img/like.png" : "/img/notlike.png"
    console.log(img);
    $('.likeImg').attr("src", img);

//    if(item.statusCode != 'AVL' && item.statusCode != 'GIV'){
//        $('.carousel-inner').addClass("div-blur");
//        $('.carousel-inner img').addClass("img-blur");
//    } else{
//        $('.carousel-inner').removeClass("div-blur");
//        $('.carousel-inner img').removeClass("img-blur");
//    }

    let btnTxt = status == "UNAV" ? "중단해제" : "일시중단";
    $('#updateStatBtn').text(btnTxt);

}

function updateItemDetail(){
    let itemGroup = url.pathname.includes("rent") ? "rent" : "give";
    let itemNo = urlSearch.get("itemNo");

    $.ajax({
        url: `/share/${itemGroup}/updateDetail?itemNo=${itemNo}`,
        type: "get",
        success: function(data){
            console.log(data);
            renderItemDetail(data.item);

        },
        error: function(data){
            alert(data.msg);
        }

     })


}

function shaRequest(item) {
    console.log(item);
    let returnDate = $('#datePicker').val();

    if(item.itemGroup == 'rent'){
        if(returnDate == ''){
            alert("반납희망일을 설정해주세요.");
            return;
        }
    }

    let data = {
        reqItem: item.itemNo,
        returnDate: returnDate,
        reqGroup: item.itemGroup,
        ownerNo: item.ownerNo
    }
    insertReq(data);

}

function insertReq(data){
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/share/request",
        type: "post",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        data: data,
        success: function(msg){
            alert(msg);
            updateItemDetail();
        },
        error: function(msg){
            alert(msg);
        }
    })
}

function shaLike(item) {
    console.log(item);
    updateShaLike(item.itemNo)
}

function updateShaLike(itemNo){

    $.ajax({
        url: `/share/like?itemNo=${itemNo}`,
        type: "get",
        success: function(msg){
            console.log(msg);
            updateItemDetail();
        },
        error: function(msg){
            console.log(msg);
        }
    })
}




//function updateItStat(item){
//    console.log(item);
//    if(item.statusCode == "RNT"){
//        alert("현재 대여중인 물건입니다. '반납완료' 처리 후 일시중단이 가능합니다.");
//        return;
//    }
//    let itemNo = item.itemNo;
//    let status = item.statusCode == "AVL" ? "UNAV" : "AVL";
//    let url = `/share/updateItStat2?itemNo=${itemNo}&statusCode=${status}`
//
//    $.ajax({
//
//        url: url,
//        type: "get",
//        success: function(data){
//            console.log(data);
//            let btnTxt = status == "UNAV" ? "중단해제" : "일시중단";
//            $('#updateStatBtn').text(btnTxt);
//
//
//        },
//        error: function(data){
//            alert(data.msg);
//
//
//        }
//
//
//    })
//
//}


