import random

from retrying import retry


@retry
def do_something_unreliable():
    if random.randint(0, 10) > 1:
        print('failure')
        raise NameError("failure")
    else:
        return "success"


# print(do_something_unreliable())


@retry
def never_give_up_never_surrender():
    print(("永远重试忽略异常，不在重试之间等待"))
    raise Exception()


# never_give_up_never_surrender()


def retry_if_io_error(exception):
    return isinstance(exception, IOError)


@retry(retry_on_exception=retry_if_io_error)
def might_io_error():
    raise IOError(print("永远重试，无需等待。"))


# might_io_error()


from retrying import retry


def retry_if_result_none(result):
    """如果我们应该重试，则返回True（在本例中，结果为None），否则返回False"""
    return result is None


@retry(retry_on_result=retry_if_result_none)
def might_return_none():
    print("如果返回值为None，则永远重试忽略异常而不等待")
    return 1


might_return_none()
