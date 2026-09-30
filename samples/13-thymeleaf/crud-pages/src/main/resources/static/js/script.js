function showAlert(button) {
  const message = button?.dataset?.alertMessage || "Button Clicked!";
  alert(message);
}
