const bookForm = document.getElementById('bookForm');
const eCheckBoxAuthors = document.getElementsByName("authors");
const tBody = document.getElementById("tBody");
const ePagination = document.getElementById("pagination");
const eSearch = document.getElementById("search");
const formBody = document.getElementById("formBody");

let bookSeleted = {};

let pageable = {
    page: 1,
    search: ''
}

let categories;

let types;

let idImages = [];


bookForm.onsubmit = async (e) => {
    e.preventDefault();

    let data = getDataFromForm(bookForm);

    console.log(data)

    data = {
        ...data,
        category: {
            id: data.category
        },
        idAuthors: Array.from(eCheckBoxAuthors)
            .filter(e => e.checked)
            .map(e => e.value),
        files: idImages.map(e => {
            return{
                id: e
            }
        }),
        id: bookSeleted.id
    }

    if(bookSeleted.id){
        await editBook(data);
    } else {
        await createBook(data);
    }
    await getList();

}

async function getCategoriesSelectOption(){
    const res = await fetch('api/categories');
    return await res.json();
}

async function getAuthorsSelectOption(){
    const res = await fetch('api/authors');
    return await res.json();
}

async function getList(){
    const response = await fetch(`/api/books?page=${pageable.page - 1 || 0}&search=${pageable.search || ''}`);

    if(!response.ok){
        throw new Error("Failed to fetch data");
    }

    const  result = await response.json();

    pageable = {
        ...pageable,
        ...result
    };
    genderPagination();
    renderTBody(result.content);
    return result;
}

function renderTBody(items) {
    let str = '';
    items.forEach(e => {
        str += renderItemStr(e);
    })
    tBody.innerHTML = str;
}

const genderPagination = () => {
    ePagination.innerHTML = '';
    let str = '';
    //generate preview truoc
    str += `<li class="page-item ${pageable.first ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
            </li>`
    //generate 1234

    for (let i = 1; i <= pageable.totalPages; i++) {
        str += ` <li class="page-item ${(pageable.page) === i ? 'active' : ''}" aria-current="page">
      <a class="page-link" href="#">${i}</a>
    </li>`
    }
    //
    //generate next truoc
    str += `<li class="page-item ${pageable.last ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Next</a>
            </li>`
    //generate 1234
    ePagination.innerHTML = str;

    const ePages = ePagination.querySelectorAll('li'); // lấy hết li mà con của ePagination
    const ePrevious = ePages[0];
    const eNext = ePages[ePages.length-1]

    ePrevious.onclick = () => {
        if(pageable.page === 1){
            return;
        }
        pageable.page -= 1;
        getList();
    }
    eNext.onclick = () => {
        if(pageable.page === pageable.totalPages){
            return;
        }
        pageable.page += 1;
        getList();
    }
    for (let i = 1; i < ePages.length - 1; i++) {
        if(i === pageable.page){
            continue;
        }
        ePages[i].onclick = () => {
            pageable.page = i;
            getList();
        }
    }
}

function renderItemStr(item) {
    const imagesHTML = item.images.map(imageUrl => `<img src="${imageUrl}" alt="" />`).join('');
    return `<tr>
                    <td>
                        ${item.id}
                    </td>
                    <td title="${item.description}">
                        ${item.title}
                    </td>
                    <td class="image-container">
                        ${imagesHTML}
                    </td>
                    <td>
                        ${item.publishDate}
                    </td>
                    <td>
                        ${item.price}
                    </td>
                    <td>
                        ${item.type}
                    </td>
                    <td>
                        ${item.category}
                    </td>
                    <td>
                        ${item.authors}
                    </td>
                    <td>
                        <div class="d-flex">
                        <button class="dropdown-item" onclick="showEdit(${item.id})"
                        data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                        ><i class="bx bx-edit-alt me-1"></i> Edit</button
                            >
                        <button class="dropdown-item" onclick="deleteBook(${item.id})"
                        ><i class="bx bx-trash me-1"></i> Delete</button
                        >
                        </div>
              </div>
            </div>
                    </td>
                </tr>`
}

window.onload = async () => {

    categories = await getCategoriesSelectOption();
    types = await getAuthorsSelectOption();
    await getList();


}

function showCreate(){
    $('#staticBackdropLabel').text('Create Book');
    clearForm();
    renderForm(formBody, getDataInput())
}

function clearForm(){
    bookForm.reset();
    bookSeleted = {};
    // document.querySelector('.errorCheckbox').textContent = "";
    $('.errorCheckbox').text('');

    idImages = [];
    const imgEle = document.getElementById("images");
    const imgOld = imgEle.querySelectorAll("img, span");
    Array.from(imgOld).forEach((img) => {
        img.remove();
    });


    const imgDefault = document.createElement("img");
    imgDefault.src = "../assets/img/img.png";
    imgDefault.classList.add("avatar-preview");
    imgDefault.style = "height: 100px; width: 100px";
    imgEle.append(imgDefault);

}

function getDataInput() {
    return [
        {
            label: "Title",
            name: "title",
            value: bookSeleted.title,
            required: true,
            pattern: "^[A-Za-z ]{6,20}",
            message: "Title phai tu 6-20 ki tu"
        },
        {
            label: "Type",
            name: "type",
            value: bookSeleted.type,
            type: "select",
            required: true,
            options: [{value: "SINGLE_VOLUME", name: "SINGER_VOLUME"}, {value: "MULTIPLE_VOLUME", name: "MULTIPLE_VOLUME"}],
            message: "Vui long chon loai sach"
        },
        {
            label: 'Category',
            name: 'category',
            value: bookSeleted.idCategory,
            type: "select",
            required: true,
            options: categories,
            message: 'Vui long chon danh muc sach'
        },
        {
            label: 'PublishDate',
            name: 'publishDate',
            value: bookSeleted.publishDate,
            required: true,
            type: "date",
            message: 'Please choose Date'
        },
        {
            label: 'Price',
            name: 'price',
            value: bookSeleted.price,
            pattern: "[1-9][0-9]{1,10}",
            message: 'Price errors',
            required: true
        },
        {
            label: 'Description',
            name: 'description',
            value: bookSeleted.description,
            pattern: "^[A-Za-z ]{6,120}",
            message: "Description must have minimum is 6 characters and maximum is 20 characters",
            required: true
        }
    ]
}

function handleCheckboxChange() {
    const checkboxes = document.querySelectorAll('input[name="authors"]:checked');
    const errorCheckbox = document.querySelector('.errorCheckbox');
    if (checkboxes.length > 0) {
        errorCheckbox.textContent = '';
    } else {
        errorCheckbox.textContent = 'Vui lòng chọn tác giả';
    }
}

async function createBook(data) {
    const checkboxes = document.querySelectorAll('input[name="authors"]:checked');
    if (checkboxes.length > 0) {
        const response = await fetch('api/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        webToast.Success({
            status: 'Thêm thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
        $('#staticBackdrop').modal('hide');
    }

}

async function showEdit(id){
    $('#staticBackdropLabel').text('Edit Book');
    clearForm();
    bookSeleted = await findById(id);
    bookSeleted.idAuthors.forEach(idAuthor => {
        for (let i = 0; i < eCheckBoxAuthors.length; i++) {
            if (idAuthor === +eCheckBoxAuthors[i].value) {
                eCheckBoxAuthors[i].checked = true;
            }
        }
    })
    showImgInForm(bookSeleted.images);
    renderForm(formBody, getDataInput());
}

// function showImgInForm(images) {
//     const imgEle = document.getElementById("images");
//     const imgOld = imgEle.querySelectorAll("img");
//     for (let i = 0; i < imgOld.length; i++) {
//         imgEle.removeChild(imgOld[i])
//     }
//     const avatarDefault = document.createElement('img');
//     avatarDefault.src = '/assets/img/img.png';
//     avatarDefault.classList.add('avatar-preview');
//     imgEle.append(avatarDefault)
//     images.forEach((img, index) => {
//         let image = document.createElement('img');
//         image.src = img;
//         image.classList.add('avatar-preview');
//         imgEle.append(image)
//     })
// }


function showImgInForm(images) {
    const imgEle = document.getElementById("images");
    const input = document.getElementById("file");
    const imgOld = imgEle.querySelectorAll("img");
    for (let i = 0; i < imgOld.length; i++) {
        imgEle.removeChild(imgOld[i])
    }
    const avatarDefault = document.createElement('img');
    avatarDefault.src = '/assets/img/img.png';
    avatarDefault.classList.add('avatar-preview');
    imgEle.append(avatarDefault)

    images.forEach((img, index) => {
        let imageContainer = document.createElement('div');
        // imageContainer.classList.add('image-container');

        let image = document.createElement('img');
        image.src = img;
        image.classList.add('avatar-preview');
        imageContainer.append(image);

        let deleteButton = document.createElement('span');
        deleteButton.innerText = 'X';
        deleteButton.classList.add('delete-button');
        deleteButton.addEventListener('click', () => {

            input.disabled = true;
            removeImage(index); // Gọi hàm xóa ảnh khi click vào dấu X
            imageContainer.remove(); // Xóa container chứa ảnh và nút X
        });
        imageContainer.append(deleteButton);

        imgEle.append(imageContainer);
    });
}

async function removeImage(index) {
    try {
        const imgToDelete = bookSeleted.images[index];
        console.log(imgToDelete);
       const formData = new FormData;
       formData.append("url", imgToDelete);

        const response = await fetch("api/bookImages", {
            method: 'DELETE',
            body:formData,
        });

        if (response.ok) {
            // Xóa ảnh khỏi mảng images
            bookSeleted.images.splice(index, 1);
            const input = document.getElementById("file");
            input.disabled = false;
            console.log('Image deleted successfully');
        } else {
            console.error('Error deleting image:', response.status);
        }
    } catch (error) {
        console.error('Error deleting image:', error);
    }
}
async function findById(id) {
    const response = await fetch('api/books/' + id);
    return await response.json();
}

async function editBook(data) {
    const checkboxes = document.querySelectorAll('input[name="authors"]:checked');
    if (checkboxes.length > 0) {
        const response = await fetch('api/books/' + data.id, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        webToast.Success({
            status: 'Sửa thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
        $('#staticBackdrop').modal('hide');
    }
}

async function deleteBook(id) {
    var confirmBox = webToast.confirm("Ban muon xoa sach nay khong??");
    confirmBox.click(async function() {
        const response = await fetch('api/books/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(id)
        });
        if (response.ok) {
            webToast.Success({
                status: 'Xóa thành công',
                message: '',
                delay: 2000,
                align: 'topright'
            });

            await getList();
        } else {
            webToast.Danger({
                status: 'Xóa lỗi rồi',
                message: '',
                delay: 2000,
                align: 'topright'
            });
        }
    })
}

// async function previewImage(evt) {
//     if (evt.target.files.length === 0){
//         return;
//     }
//     idImages = [];
//
//     const imgEle = document.getElementById("images");
//     const imgOld = imgEle.querySelectorAll("img");
//     for (let i = 0; i < imgOld.length; i++){
//         imgEle.removeChild(imgOld[i]);
//     }
//
//     const files = evt.target.files;
//     for (let i = 0; i < files.length; i++){
//         const file = files[i];
//         await previewImageFile(file);
//
//         if (file){
//             disableSaveChangesButton();
//             //tao formData va them file duoc chon
//             const formData = new FormData();
//             formData.append("avatar", file);
//             formData.append("fileType", "image");
//
//             try{
//                 const response = await fetch("api/bookImages", {
//                     method: "POST",
//                     body: formData
//                 });
//
//                 if (response.ok){
//                     const result = await response.json();
//                     if (result) {
//                         const id = result.id;
//                         idImages.push(id);
//                     } else {
//                         console.error("Image ID not found in the response");
//                     }
//                 } else {
//                     console.error("Failed to upload image:", response.statusText);
//                 }
//
//             }catch (error){
//                 console.error("An error occurred:", error);
//             }
//             enableSaveChangesButton();
//         }
//     }
//
// }

async function previewImage(evt) {
    if (evt.target.files.length === 0){
        return;
    }
    idImages = [];

    const files = evt.target.files;
    for (let i = 0; i < files.length; i++){
        const file = files[i];
        await previewImageFile(file);

        if (file){
            disableSaveChangesButton();
            //tao formData va them file duoc chon
            const formData = new FormData();
            formData.append("avatar", file);
            formData.append("fileType", "image");

            try{
                const response = await fetch("api/bookImages", {
                    method: "POST",
                    body: formData
                });

                if (response.ok){
                    const result = await response.json();

                    //

                    if (result && result.id) {
                        const id = result.id;
                        console.log(id)
                        const imgEle = document.getElementById("images");
                        const imageContainer = imgEle.lastChild;
                        const img = imageContainer.querySelector("img");
                        // img.dataset.id = id;
                        idImages.push(id);

                        const deleteButton = imageContainer.querySelector(".delete-button");
                        deleteButton.addEventListener("click", () => {
                            const input = document.getElementById("file");
                            input.disabled = true;
                            deleteImage(id);
                            imageContainer.remove();
                        });
                    } else {
                        console.error("Image ID not found in the response");
                    }
                } else {
                    console.error("Failed to upload image:", response.statusText);
                }

            }catch (error){
                console.error("An error occurred:", error);
            }
            enableSaveChangesButton();
        }
    }

}

async function deleteImage(id) {
    try {
        const response = await fetch(`api/bookImages/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            const input = document.getElementById("file");
            input.disabled = false;
            console.log("Image deleted from the database.");
        } else {
            console.error("Failed to delete image from the database:", response.statusText);
        }
    } catch (error) {
        console.error("An error occurred:", error);
    }
}

// async function previewImageFile(file){
//     const reader = new FileReader();
//     reader.onload = function (){
//         const imgEle = document.getElementById("images");
//         const img = document.createElement("img");
//         img.src = reader.result;
//         img.classList.add("avatar-preview");
//         imgEle.append(img);
//
//     };
//     reader.readAsDataURL(file);
// }

async function previewImageFile(file){
    const reader = new FileReader();
    reader.onload = function (){
        const imgEle = document.getElementById("images");

        const imageContainer = document.createElement('div');

        const img = document.createElement("img");

        img.src = reader.result;
        img.classList.add("avatar-preview");
        imageContainer.append(img);

        console.log(file)

        let deleteButton = document.createElement('span');
        deleteButton.innerText = 'X';
        deleteButton.classList.add('delete-button');


        imageContainer.append(deleteButton);

        imgEle.append(imageContainer);

    };
    reader.readAsDataURL(file);
}


function disableSaveChangesButton() {
    const saveChangesButton = document.getElementById('saveChangesButton');
    saveChangesButton.disabled = true;
}
function enableSaveChangesButton() {
    const saveChangesButton = document.getElementById('saveChangesButton');
    saveChangesButton.disabled = false;
}


