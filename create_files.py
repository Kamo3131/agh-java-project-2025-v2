import zipfile
import os

os.mkdir("saved_files")

for i in range(1, 21):
    with open(f"file{i}.txt", "w") as f:
        f.write(f"this is file number {i}\nthis is the second line of file number {i}")

    with zipfile.ZipFile(f"saved_files/file{i}.zip", "x", zipfile.ZIP_DEFLATED) as zipped:
        zipped.write(f"file{i}.txt")

    os.remove(f"file{i}.txt")
