:: run in cmd to create a legacy venv for testing
:: @echo off
echo "Creating virtualenv..."
python -m venv ..\..\..\legacy_venv
echo "Installing dependencies..."
..\..\..\legacy_venv\Scripts\activate.bat & python -m pip install weasyprint==52.5
echo "Done."