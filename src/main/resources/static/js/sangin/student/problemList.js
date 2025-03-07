    const hardBtn = document.querySelector("#hard_btn");
    let colorInterval;

    hardBtn.addEventListener("mouseenter", () => {
    clearInterval(colorInterval);
    colorInterval = setInterval(() => {
    let randomColor1 = `#${Math.floor(Math.random() * 16777215).toString(16)}`;
    let randomColor2 = `#${Math.floor(Math.random() * 16777215).toString(16)}`;
    //let randomScale = (Math.random() * 0.5 + 1).toFixed(2);

    hardBtn.style.backgroundColor = randomColor1;
    hardBtn.style.color = randomColor2;
    //hardBtn.style.transform = `scale(${randomScale})`;
}, 100);
});

    hardBtn.addEventListener("mouseleave", () => {
    clearInterval(colorInterval);
    hardBtn.style.backgroundColor = "#ddd";
    hardBtn.style.color = "black";
    //hardBtn.style.transform = "scale(1)";
});

    const testBtn = document.querySelector("#test_btn");
    testBtn.addEventListener("mouseenter", () => {
    testBtn.innerHTML = '';
    testBtn.innerHTML = 'TEST';
});
    testBtn.addEventListener("mouseleave", () => {
    testBtn.innerHTML = '';
    testBtn.innerHTML = '시험';
});
