# 说明
## Experiment Helper 程序
BlanceBoardExpiermentHelper 包含了可以执行平衡板链接的数据收集程序[x86-64 平台，基于 C++ 和 Qt 编写的平衡板蓝牙数据获取程序 BalanceBoardConnector]，以及在 Windows x86-64 平台用来模拟鼠标点击上述蓝牙 GUI 程序并记录开始时间的帮助程序 [x86-64 平台，基于 pywin32 库的 Python 3 程序 pyButtonTrigger]。实际试验时，首先连接平衡板到蓝牙，然后启动连接程序，之后将其放置到前台活动窗口，调用 Python 3 程序模拟点击并且记录点击时数据的开始时间。

Python 外围程序运行代码需要安装 Python 3，此外需要 pip 安装如下包：pywin32。

## DataAlign Processer 程序
BlanceBoardDataAlignProcesser 有两个版本：

### Python 版本
Python 版本包含一个 ipynb 交互笔记本程序，可使用 ipython notebook 打开。此外还有一个 py 程序，可直接执行，从命令行输入必要信息执行数据对齐，一般而言，其需要被试姓名、编号、开始的两个时间戳和结束的两个时间戳。此时间戳由 Helper 中 Pyhton 3 程序生成，保存在 python_runtime_record.log 文件中。

Python 版本需要 Python 3 环境，需要用到以下包：pandas, numpy, xlrd, xlwt。

### Java 版本
Java 版本包含一个等价 Python 版本的 BBDA.jar，在 JDK8 平台上可直接运行，输入上述信息后可同样执行数据对齐，其利用了多核心能力，比 Python 数据快 10 倍左右。此外，BBDAManager.jar 是一个多被试管理的信息收集系统，其数据保存在 data.dta 文件中，可批量生成 BBDA.jar 对齐命令，一键对多个被试进行数据对齐。

此外，本目录下还包括 Java 程序构建的源代码，基于 Maven 包管理器。

Java 版本需要 JRE8/JDK8 环境。