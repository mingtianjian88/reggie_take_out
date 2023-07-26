package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.mapper.AddressBookMapper;
import com.zhang.pojo.AddressBook;
import com.zhang.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-07-23 20:32
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
