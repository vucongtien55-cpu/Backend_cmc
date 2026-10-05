let editingId = null;

async function loadStudents() {
    const res = await fetch("/api/Student");
    const students = await res.json();

    const tbody = document.getElementById("studentTableBody");
    tbody.innerHTML = "";

    students.forEach(student => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.age}</td>
            <td>${student.email}</td>
            <td>${student.address}</td>
            <td>${student.phone || ""}</td>
            <td>
                <button class="action-btn edit-btn" data-id="${student.id}">Sửa</button>
                <button class="action-btn delete-btn" data-id="${student.id}">Xóa</button>
            </td>
        `;

        tbody.appendChild(row);
    });

    bindActions();
}

function bindActions() {
    document.querySelectorAll(".edit-btn").forEach(btn => {
        btn.addEventListener("click", async function () {
            const id = this.getAttribute("data-id");
            await editStudent(id);
        });
    });

    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", async function () {
            const id = this.getAttribute("data-id");
            await deleteStudent(id);
        });
    });
}

async function editStudent(id) {
    const res = await fetch(`/api/Student/${id}`);
    const student = await res.json();

    document.getElementById("name").value = student.name;
    document.getElementById("age").value = student.age;
    document.getElementById("email").value = student.email;
    document.getElementById("address").value = student.address;
    document.getElementById("phone").value = student.phone || "";

    editingId = id;
    document.getElementById("saveBtn").textContent = "Cập nhật";
    window.scrollTo({ top: 0, behavior: "smooth" });
}

async function deleteStudent(id) {
    if (!confirm("Bạn có chắc muốn xóa sinh viên này không?")) return;

    await fetch(`/api/Student/${id}`, {
        method: "DELETE"
    });

    loadStudents();
}

document.getElementById("studentForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const student = {
        name: document.getElementById("name").value,
        age: Number(document.getElementById("age").value),
        email: document.getElementById("email").value,
        address: document.getElementById("address").value,
        phone: document.getElementById("phone").value
    };

    if (editingId !== null) {
        await fetch(`/api/Student/${editingId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(student)
        });
    } else {
        await fetch("/api/Student", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(student)
        });
    }

    this.reset();
    editingId = null;
    document.getElementById("saveBtn").textContent = "Lưu";

    loadStudents();
});

loadStudents();