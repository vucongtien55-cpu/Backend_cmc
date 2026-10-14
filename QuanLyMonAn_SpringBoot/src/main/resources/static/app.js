const $ = id => document.getElementById(id);
let currentLang = localStorage.getItem('dishLang') || 'en';
let currentDetailId = null;
const words = {
 en:{title:'Dish Management',subtitle:'Manage your menu',search:'Search & Filter',filter:'Filter',reset:'Reset',dishes:'Dishes',addDish:'+ Add dish',name:'Name',category:'Category',price:'Price',time:'Prep. time',status:'Status',actions:'Actions',available:'Available',description:'Description',save:'Save',cancel:'Cancel',detail:'Dish details & images',close:'Close',thumbnail:'Set as thumbnail',addImage:'Add image',allCategories:'All categories',allStatuses:'All statuses',default:'Default',ascending:'Ascending',descending:'Descending',edit:'Edit',view:'Details',delete:'Delete',yes:'Available',no:'Unavailable',quick:'Quick',regular:'Regular',empty:'No dishes found.',saved:'Saved successfully.',confirmDelete:'Delete this dish?',badRange:'Invalid price range.',noCategory:'No category',makeThumbnail:'Make thumbnail',remove:'Remove',imageAdded:'Image added.'},
 vi:{title:'Quản lý món ăn',subtitle:'Quản lý thực đơn',search:'Tìm kiếm & lọc',filter:'Lọc',reset:'Đặt lại',dishes:'Danh sách món ăn',addDish:'+ Thêm món',name:'Tên món',category:'Loại món',price:'Giá',time:'Thời gian chế biến',status:'Trạng thái',actions:'Thao tác',available:'Đang bán',description:'Mô tả',save:'Lưu',cancel:'Hủy',detail:'Chi tiết món & hình ảnh',close:'Đóng',thumbnail:'Đặt làm ảnh đại diện',addImage:'Thêm ảnh',allCategories:'Tất cả loại món',allStatuses:'Tất cả trạng thái',default:'Mặc định',ascending:'Tăng dần',descending:'Giảm dần',edit:'Sửa',view:'Chi tiết',delete:'Xóa',yes:'Đang bán',no:'Ngừng bán',quick:'Nhanh',regular:'Thông thường',empty:'Không tìm thấy món ăn.',saved:'Đã lưu thành công.',confirmDelete:'Bạn có chắc muốn xóa món này?',badRange:'Khoảng giá không hợp lệ.',noCategory:'Chưa phân loại',makeThumbnail:'Đặt làm ảnh đại diện',remove:'Xóa',imageAdded:'Đã thêm ảnh.'}
};
const w = key => words[currentLang][key] || key;
function translateStatic(){
 document.querySelectorAll('[data-i18n]').forEach(el=>el.textContent=w(el.dataset.i18n));
 $('langBtn').textContent=currentLang==='en'?'Tiếng Việt':'English';
 $('name').placeholder=currentLang==='en'?'Dish name':'Tên món';
 $('minPrice').placeholder=currentLang==='en'?'Min price':'Giá từ';
 $('maxPrice').placeholder=currentLang==='en'?'Max price':'Giá đến';
 $('imageUrl').placeholder=currentLang==='en'?'Image URL':'Đường dẫn ảnh';
 $('category').options[0].textContent=w('allCategories'); $('available').options[0].textContent=w('allStatuses');
 $('sortBy').options[0].textContent=w('default'); $('sortBy').options[1].textContent=w('price'); $('sortBy').options[2].textContent=w('time');
 $('direction').options[0].textContent=w('ascending'); $('direction').options[1].textContent=w('descending');
}
async function api(url,options={}) {
 const response=await fetch(url,{headers:{'Content-Type':'application/json'},...options});
 if(!response.ok){let msg=`HTTP ${response.status}`;try{const b=await response.json();msg=b.message||b.detail||msg}catch{}throw new Error(msg)}
 if(response.status===204)return null;return response.json();
}
function showMessage(message,isError=false){$('message').textContent=message;$('message').style.color=isError?'#b91c1c':'#15803d'}
async function loadCategories(){
 const cats=await api('/api/categories');
 for(const id of ['category','dishCategory']){
  const el=$(id),selected=el.value;el.innerHTML=id==='category'?`<option value="">${w('allCategories')}</option>`:'';
  cats.forEach(c=>el.add(new Option(c.name,c.id)));if(selected)el.value=selected;
 }
}
function filterParams(){
 const p=new URLSearchParams();
 if($('name').value.trim())p.set('name',$('name').value.trim());
 if($('category').value)p.set('categoryId',$('category').value);
 if($('available').value!=='')p.set('available',$('available').value);
 if($('minPrice').value!=='')p.set('minPrice',$('minPrice').value);
 if($('maxPrice').value!=='')p.set('maxPrice',$('maxPrice').value);
 p.set('sortBy',$('sortBy').value);p.set('direction',$('direction').value);
 return p;
}
async function loadDishes(){
 try{
  const min=$('minPrice').value,max=$('maxPrice').value;
  if(min!==''&&max!==''&&Number(min)>Number(max)){showMessage(w('badRange'),true);return}
  const dishes=await api('/api/dishes?'+filterParams().toString());
  const body=$('dishRows');body.innerHTML='';
  if(!dishes.length){body.innerHTML=`<tr><td colspan="7">${w('empty')}</td></tr>`;return}
  dishes.forEach(d=>{
   const tr=document.createElement('tr');
   const category=d.dishCategory?.name||w('noCategory');
   const available=d.isAvailable===true;
   tr.innerHTML=`<td>${d.id}</td><td></td><td></td><td>${Number(d.price).toFixed(2)}</td><td>${d.preparationTime} min · ${d.preparationTime<=15?w('quick'):w('regular')}</td><td><span class="status ${available?'':'off'}">${available?w('yes'):w('no')}</span></td><td><div class="row-actions"><button data-action="view">${w('view')}</button><button data-action="edit">${w('edit')}</button><button data-action="delete">${w('delete')}</button></div></td>`;
   tr.children[1].textContent=d.name;tr.children[2].textContent=category;
   tr.querySelector('[data-action=view]').onclick=()=>showDetail(d.id);
   tr.querySelector('[data-action=edit]').onclick=()=>editDish(d.id);
   tr.querySelector('[data-action=delete]').onclick=()=>deleteDish(d.id);
   body.appendChild(tr);
  });
 }catch(e){showMessage(e.message,true)}
}
function openEditor(){ $('editor').hidden=false;$('editor').scrollIntoView({behavior:'smooth'}); }
function closeEditor(){ $('editor').hidden=true;$('dishForm').reset();$('dishId').value=''; }
$('newBtn').onclick=async()=>{try{await loadCategories();closeEditor();$('dishAvailable').checked=true;$('editorTitle').textContent=w('addDish');openEditor()}catch(e){showMessage(e.message,true)}};
$('cancelBtn').onclick=closeEditor;
async function editDish(id){
 try{await loadCategories();const d=await api('/api/dishes/'+id);$('dishId').value=d.id;$('dishName').value=d.name;$('dishPrice').value=d.price;$('dishTime').value=d.preparationTime;$('dishCategory').value=d.dishCategory?.id||'';$('dishAvailable').checked=!!d.isAvailable;$('dishDescription').value=d.description||'';$('editorTitle').textContent=w('edit');openEditor()}catch(e){showMessage(e.message,true)}
}
$('dishForm').onsubmit=async e=>{
 e.preventDefault();
 const id=$('dishId').value;
 const data={name:$('dishName').value.trim(),price:Number($('dishPrice').value),preparationTime:Number($('dishTime').value),isAvailable:$('dishAvailable').checked,description:$('dishDescription').value,categoryId:Number($('dishCategory').value)};
 try{await api(id?'/api/dishes/'+id:'/api/dishes',{method:id?'PUT':'POST',body:JSON.stringify(data)});closeEditor();showMessage(w('saved'));await loadDishes()}catch(err){showMessage(err.message,true)}
};
async function deleteDish(id){if(!confirm(w('confirmDelete')))return;try{await api('/api/dishes/'+id,{method:'DELETE'});showMessage(w('saved'));await loadDishes()}catch(e){showMessage(e.message,true)}}
async function showDetail(id){
 try{
  currentDetailId=id;const d=await api('/api/dishes/'+id);$('detail').hidden=false;
  $('detailContent').innerHTML=`<h3></h3><p></p><p></p>`;
  $('detailContent').children[0].textContent=d.name;
  $('detailContent').children[1].textContent=`${w('category')}: ${d.dishCategory?.name||w('noCategory')} · ${d.price}`;
  $('detailContent').children[2].textContent=`${d.preparationTime} min · ${d.preparationTime<=15?w('quick'):w('regular')} · ${d.isAvailable?w('yes'):w('no')}`;
  const imgs=await api('/api/dishes/'+id+'/images');const box=$('images');box.innerHTML='';
  if(!imgs.length)box.textContent=currentLang==='en'?'No images yet.':'Chưa có ảnh.';
  imgs.forEach(img=>{
   const card=document.createElement('div');card.className='image-card';
   const image=document.createElement('img');image.src=img.imageUrl;image.alt=d.name;image.onerror=()=>image.style.opacity='.2';card.appendChild(image);
   const p=document.createElement('p');p.textContent=img.isThumbnail?'★ '+(currentLang==='en'?'Thumbnail':'Ảnh đại diện'):img.imageUrl;card.appendChild(p);
   if(!img.isThumbnail){const b=document.createElement('button');b.textContent=w('makeThumbnail');b.onclick=async()=>{try{await api('/api/images/'+img.id+'/thumbnail',{method:'PUT'});await showDetail(id)}catch(e){showMessage(e.message,true)}};card.appendChild(b)}
   const del=document.createElement('button');del.className='secondary';del.style.marginLeft='5px';del.textContent=w('remove');del.onclick=async()=>{try{await api('/api/images/'+img.id,{method:'DELETE'});await showDetail(id)}catch(e){showMessage(e.message,true)}};card.appendChild(del);
   box.appendChild(card);
  });
  $('detail').scrollIntoView({behavior:'smooth'});
 }catch(e){showMessage(e.message,true)}
}
$('closeDetail').onclick=()=>{$('detail').hidden=true;currentDetailId=null};
$('imageForm').onsubmit=async e=>{e.preventDefault();if(!currentDetailId)return;try{await api(`/api/dishes/${currentDetailId}/images`,{method:'POST',body:JSON.stringify({imageUrl:$('imageUrl').value.trim(),isThumbnail:$('isThumbnail').checked})});$('imageUrl').value='';$('isThumbnail').checked=false;showMessage(w('imageAdded'));await showDetail(currentDetailId)}catch(err){showMessage(err.message,true)}};
$('filterForm').onsubmit=e=>{e.preventDefault();loadDishes()};
$('filterForm').onreset=()=>setTimeout(loadDishes,0);
$('langBtn').onclick=()=>{currentLang=currentLang==='en'?'vi':'en';localStorage.setItem('dishLang',currentLang);translateStatic();loadCategories().then(loadDishes);if(currentDetailId)showDetail(currentDetailId);};
async function start(){translateStatic();try{await loadCategories();await loadDishes()}catch(e){showMessage('Cannot connect to backend: '+e.message,true)}}
start();
