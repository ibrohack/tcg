document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('profilePicture');
    const cropModal = document.getElementById('cropModal');
    const cropImage = document.getElementById('cropImage');
    const closeCropModal = document.getElementById('closeCropModal');
    const cancelCrop = document.getElementById('cancelCrop');
    const applyCrop = document.getElementById('applyCrop');
    
    // Handle both cases where we are previewing
    const profileImagePreviews = document.querySelectorAll('#profileImagePreview');
    const profileImagePlaceholder = document.getElementById('profileImagePlaceholder');

    let cropper = null;
    let originalFilename = 'profile.jpg';

    if (!fileInput || !cropModal) return;

    fileInput.addEventListener('change', function (e) {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0];
            originalFilename = file.name;

            const reader = new FileReader();
            reader.onload = function (event) {
                // Ensure image load before cropper init
                cropImage.onload = function() {
                    // Show modal
                    cropModal.classList.remove('hidden');
                    
                    if (cropper) {
                        cropper.destroy();
                    }
                    
                    cropper = new Cropper(cropImage, {
                        aspectRatio: 1,
                        viewMode: 1,
                        dragMode: 'move',
                        autoCropArea: 1,
                        background: true,
                        restore: false,
                        guides: true,
                        center: true,
                        highlight: false,
                        cropBoxMovable: true,
                        cropBoxResizable: true,
                        toggleDragModeOnDblclick: false,
                    });
                };
                cropImage.src = event.target.result;
            };
            reader.readAsDataURL(file);
        }
    });

    function hideModal() {
        cropModal.classList.add('hidden');
        if (cropper) {
            cropper.destroy();
            cropper = null;
        }
        // If user cancels and hasn't successfully cropped before, we clear the file input 
        // to prevent uploading the uncropped image, though ideally we could just leave whatever was there.
        // For simplicity, we just close the modal.
    }

    closeCropModal.addEventListener('click', hideModal);
    cancelCrop.addEventListener('click', function() {
        // Clear file input on cancel so we don't submit uncropped
        fileInput.value = '';
        hideModal();
    });

    applyCrop.addEventListener('click', function () {
        if (!cropper) return;

        // Get cropped canvas scaled to exactly 100x100
        const canvas = cropper.getCroppedCanvas({
            width: 100,
            height: 100,
            imageSmoothingEnabled: true,
            imageSmoothingQuality: 'high',
        });

        // Convert the canvas to Blob
        canvas.toBlob(function (blob) {
            if (!blob) return;

            // Create a new File object from the Blob
            // Default to jpeg to ensure consistent behavior
            const croppedFile = new File([blob], originalFilename, {
                type: 'image/jpeg',
                lastModified: Date.now(),
            });

            // Update the file input's files array using DataTransfer
            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(croppedFile);
            fileInput.files = dataTransfer.files;

            // Update preview image(s) on the page
            const croppedUrl = URL.createObjectURL(blob);
            profileImagePreviews.forEach(img => {
                img.src = croppedUrl;
                img.classList.remove('hidden');
            });
            if (profileImagePlaceholder) {
                profileImagePlaceholder.classList.add('hidden');
            }

            hideModal();
        }, 'image/jpeg', 0.95);
    });
});
