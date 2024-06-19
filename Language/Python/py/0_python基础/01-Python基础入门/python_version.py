import sys

version = sys.version_info
cp_version = f"{version.major}.{version.minor}"

print(f"Python版本: {sys.version}")
print(f"CP版本: {cp_version}")

import platform

interpreter = platform.python_implementation()
print(interpreter)
print(platform.python_version())
