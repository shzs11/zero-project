import request from '@/utils/request'

// 创建错题
export function createMessage(data) {
  return request({
    url: '/topic/message/create',
    method: 'post',
    data: data
  })
}

// 更新错题
export function updateMessage(data) {
  return request({
    url: '/topic/message/update',
    method: 'put',
    data: data
  })
}

// 删除错题
export function deleteMessage(id) {
  return request({
    url: '/topic/message/delete?id=' + id,
    method: 'delete'
  })
}

// 获得错题
export function getMessage(id) {
  return request({
    url: '/topic/message/get?id=' + id,
    method: 'get'
  })
}

// 获得错题分页
export function getMessagePage(query) {
  return request({
    url: '/topic/message/page',
    method: 'get',
    params: query
  })
}

// 导出错题 Excel
export function exportMessageExcel(query) {
  return request({
    url: '/topic/message/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
