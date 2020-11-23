import pandas as pd
import os

def xlsx_to_csv(file):
    data_xls = pd.read_excel(file, index_col=0)
    data_xls.to_csv(file[:-5]+'.csv', encoding='utf-8')


if __name__ == '__main__':

    print("Start batch xlsx to csv...\n")
    print("CSV Files as follows:\n")

    csv_list = []
    for roots, dirs, files in os.walk("."):
        for file in files:
            if file.endswith(".xlsx"):
                print(file)
                csv_list.append(os.path.join(roots, file))

    print("\n")

    for file in csv_list:
        print("Convert file [%s] now" % file)
        xlsx_to_csv(file)

    print("Finished\n")