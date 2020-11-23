# ˵��
## Experiment2StandMatlabSourceCode

MATLAB ʵ�ֵ����ƽ���Ĵ��룬��ԭ����Ϊ��������΢�������޸ģ����ռ���ʵ����ʼʱ�����ݡ�

## Experiment2StandBlanceBoardHelper
ExpiermentHelper �����˿���ִ��ƽ������ӵ������ռ�����[x86-64 ƽ̨������ C++ �� Qt ��д��ƽ����������ݻ�ȡ���� BalanceBoardConnector]���Լ��� Windows x86-64 ƽ̨����ģ��������������� GUI ���򲢼�¼��ʼʱ��İ������� [x86-64 ƽ̨������ pywin32 ��� Python 3 ���� pyButtonTrigger]��ʵ������ʱ����������ƽ��嵽������Ȼ���������ӳ���֮������õ�ǰ̨����ڣ����� Python 3 ����ģ�������Ҽ�¼���ʱ���ݵĿ�ʼʱ�䡣

Python ��Χ�������д�����Ҫ��װ Python 3��������Ҫ pip ��װ���°���pywin32��

## DataAlignProcesser
DataAlignProcesser ���ڽ� MATLAB ���ݺ�ƽ������ݽ��ж��룬���������汾��

### Python �汾
Python �汾����һ�� ipynb �����ʼǱ����򣬿�ʹ�� ipython notebook �򿪡����⻹��һ�� py ���򣬿�ֱ��ִ�У��������������Ҫ��Ϣִ�����ݶ��룬һ����ԣ�����Ҫ������������š���ʼ������ʱ����ͽ���������ʱ�������ʱ����� Helper �� Pyhton 3 �������ɣ������� python_runtime_record.log �ļ��С�

Python �汾��Ҫ Python 3 ��������Ҫ�õ����°���pandas, numpy, xlrd, xlwt��

### Java �汾
Java �汾����һ���ȼ� Python �汾�� BBDA.jar���� JDK8 ƽ̨�Ͽ�ֱ�����У�����������Ϣ���ͬ��ִ�����ݶ��룬�������˶������������ Python ���ݿ� 10 �����ҡ����⣬BBDAManager.jar ��һ���౻�Թ������Ϣ�ռ�ϵͳ�������ݱ����� data.dta �ļ��У����������� BBDA.jar �������һ���Զ�����Խ������ݶ��롣

���⣬��Ŀ¼�»����� Java ���򹹽���Դ���룬���� Maven ����������Java �汾��Ҫ JRE8/JDK8 ������

#### BBDA

![](http://static2.mazhangjing.com/20190817/d84575c_2019-08-1720.45.40.gif)

�������������� csv ���ݺ�ƽ��������Զ��롣

#### BBDA_Manager

���������� BBDA �ĳ���

![](http://static2.mazhangjing.com/20190817/593462a_2019-08-1720.46.55.gif)

## DataExample

�����׶ε�ʾ������

### Excel2CSV.py
Excel �� CSV ת���������ڽ� MATLAB ������ xlsx ��ʽ����ת��Ϊ csv ��ʽ��֮���ƽ���� log �ļ�ִ�ж��룬�õ� output csv �ļ���

# ע��

- ƽ���ʹ�������ƽ��������ռ��������Ӱ�����������˵���� Windows 10 �½��� Control Panel��������Ӳ����Ȼ�󳤰�ƽ���ͬ����ť��ֱ������� Nintendo .. �豸��֮��ͬʱ�̰�ͬ������һ������û�� PIN ����������[��ϸ��̳���۲μ� Helper �ļ����´� PDF �ĵ���How to connect Nintendo Blance Board to Windows 10 Use Control Panel]��
- ���ݶ���������ʱ��ͬ���������̨���ԣ��豸�ռ��ʹ̼����ֱ������ôӻ�������ȡʱ�䣬��ʹ��ͬһ NTP ��������ntp1.aliyun.com ����������ѡ���� Control Panel ʱ�Ӻ͵���ѡ��������á�